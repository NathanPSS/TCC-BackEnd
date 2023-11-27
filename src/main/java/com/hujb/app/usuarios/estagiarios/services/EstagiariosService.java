package com.hujb.app.usuarios.estagiarios.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hujb.app.config.auth.jwt.JwtService;
import com.hujb.app.config.auth.AuthRequestBody;
import com.hujb.app.config.auth.jwt.Token;
import com.hujb.app.registros.dto.CheckInOpen;
import com.hujb.app.registros.dto.CheckInClosed;
import com.hujb.app.registros.dto.InfoCheckIn;
import com.hujb.app.registros.entities.Registro;
import com.hujb.app.registros.repositories.RegistroAssinadoRepository;
import com.hujb.app.registros.repositories.RegistroRepository;
import com.hujb.app.setores.Setor;
import com.hujb.app.usuarios.entities.Usuario;
import com.hujb.app.usuarios.enums.Role;
import com.hujb.app.usuarios.estagiarios.dto.*;
import com.hujb.app.usuarios.estagiarios.entities.Estagiario;
import com.hujb.app.usuarios.estagiarios.repositories.EstagiariosRepository;
import com.hujb.app.utils.time.FormaterDateStrings;
import jakarta.persistence.Tuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class EstagiariosService {

    private final JwtService jwtService;
    private final AuthenticationManager authManagerEstagiario;
    private final EstagiariosRepository repositoryEstagiarios;
    private  final RegistroRepository repositoryRegistros;
    private final RedisTemplate<String,Object> redis;
    private final PasswordEncoder encoder;


    public EstagiariosService(EstagiariosRepository repositoryEstagiarios,
                              RegistroRepository repositoryRegistros,
                              PasswordEncoder encoder,
                              AuthenticationManager authManager,
                              JwtService jwtService,
                              RedisTemplate<String,Object> redis
                              ) {
        this.repositoryEstagiarios = repositoryEstagiarios;
        this.repositoryRegistros = repositoryRegistros;
        this.encoder = encoder;
        this.authManagerEstagiario = authManager;
        this.jwtService = jwtService;
        this.redis = redis;
    }

    public EstagiarioJSON estagiarioJson(Estagiario estagiario){
        return new EstagiarioJSON(estagiario.getMatricula(),estagiario.getUsuario());
    }

    public EstagiarioJSON findByMatricula(String matricula){
      return this.estagiarioJson(repositoryEstagiarios.findByMatricula(matricula));
    }

    public void create(CreateEstagiarioDTO dto){
        var estagiario = new Estagiario(
                new Usuario(
                        dto.nome(),
                        Role.USER
                ),
                dto.matricula(),
                encoder.encode(dto.password())
        );
       repositoryEstagiarios.save(estagiario);
    };

    public Token authenticate(AuthRequestBody dto) {
        var estagiario = repositoryEstagiarios.findByUsername(dto.username()).orElseThrow(() -> new UsernameNotFoundException("Estagiario nao encontrado"));
          authManagerEstagiario.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.username(),
                            dto.password()
                    )
            );
     // token is 2 minutes more valid than the information of his expires, this prevent the token expire between API comunication
        var token = new Token(jwtService.generateToken(estagiario),Instant.now().minus(Duration.ofMinutes(2L)).plus(Duration.ofDays(1L)).toString());
        System.out.println(token.token());
        return token;
    }

    public void openCheckIn(Setor dto){
        var checkIn = new CheckInOpen(
               SecurityContextHolder.getContext().getAuthentication().getName(),
                Instant.now().toString(),
                UUID.randomUUID().toString(),
                dto
        );

        // Não Mude esses metodos de get e set no Redis pois ultilizando metodos primitivos de get e set ira quebrar
        redis.opsForHash().put(checkIn.username(),checkIn.username(),checkIn);
        redis.expireAt(checkIn.username(),Instant.now().plus(Duration.ofDays(1L)));

    }

    public InfoCheckIn closeCheckIn() {
      var checkIn = (CheckInOpen) redis.opsForHash().get(
              SecurityContextHolder.getContext().getAuthentication().getName(),
              SecurityContextHolder.getContext().getAuthentication().getName()
      );
        assert checkIn != null;

       var checkInClosed = new CheckInClosed(
               checkIn,
               Instant.now().toString()
       );
        redis.opsForHash().put(checkIn.username(),checkIn.username(),checkInClosed);

        return new InfoCheckIn(
                checkIn.setor().getNome(),
                repositoryEstagiarios.findByUsername(checkIn.username()).orElseThrow().getUsuario().getNome(),
                FormaterDateStrings.parseTimeHour(checkIn.createdAT()) + ":" + FormaterDateStrings.parseTimeMinute(checkIn.createdAT()),
                FormaterDateStrings.parseTimeHour(checkInClosed.closedAt()) + ":" + FormaterDateStrings.parseTimeMinute(checkInClosed.closedAt()),
                 FormaterDateStrings.parseTimeDuration(Duration.between(Instant.parse(checkInClosed.checkInOpen().createdAT()),Instant.parse(checkInClosed.closedAt()))),
                DateTimeFormatter.ofPattern("dd/MM/yyyy").format(Instant.now().atZone(ZoneId.systemDefault()))
        );
    }

    public void createRegistry(String description){
        var checkInClosed = (CheckInClosed) redis.opsForHash().get(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        assert checkInClosed != null;
        repositoryRegistros.save(new Registro(
            checkInClosed.checkInOpen().id(),
            repositoryEstagiarios.findByUsername(checkInClosed.checkInOpen().username()).orElseThrow(),
            checkInClosed.checkInOpen().setor(),
            Duration.between(Instant.parse(checkInClosed.checkInOpen().createdAT()),Instant.parse(checkInClosed.closedAt())).toString(),
            checkInClosed.checkInOpen().createdAT(),
            checkInClosed.closedAt(),
            description
        ));
        redis.opsForHash().delete(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
    }

    public String getCurrentTimeCheckIn(){
        var checkIn = (CheckInOpen) redis.opsForHash().get(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        assert checkIn != null;
        return checkIn.createdAT();
    };
    public boolean isCheckInOpen(){
        var checkIn =  redis.opsForHash().get(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        return checkIn instanceof CheckInOpen;
    };

   public AllRegistrosEstagiario getAllRegistries(){
       var username = SecurityContextHolder.getContext().getAuthentication().getName();
       return new AllRegistrosEstagiario(
               RegistryWithoutSIgn.serialize(repositoryEstagiarios.getAllRegistriesWithoutSing(username)),
               RegistrySigned.serialize(repositoryEstagiarios.getAllRegistriesSigned(username)),
               RegistryRejected.serialize(repositoryEstagiarios.getAllRegistriesRejected(username))
       );
   }

}
