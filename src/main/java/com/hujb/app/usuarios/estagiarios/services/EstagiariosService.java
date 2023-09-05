package com.hujb.app.usuarios.estagiarios.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hujb.app.config.auth.jwt.JwtService;
import com.hujb.app.config.auth.jwt.dto.AuthRequestBody;
import com.hujb.app.config.auth.jwt.dto.Token;
import com.hujb.app.registros.dto.CheckInRaw;
import com.hujb.app.registros.entities.Registro;
import com.hujb.app.registros.enums.StatusRegistro;
import com.hujb.app.registros.repositories.RegistroRepository;
import com.hujb.app.setores.Setor;
import com.hujb.app.usuarios.entities.Usuario;
import com.hujb.app.usuarios.enums.Role;
import com.hujb.app.usuarios.estagiarios.dto.EstagiarioJSON;
import com.hujb.app.usuarios.estagiarios.dto.RegistryRequestDTO;
import com.hujb.app.usuarios.estagiarios.entities.Estagiario;
import com.hujb.app.usuarios.estagiarios.repositories.EstagiariosRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class EstagiariosService {

    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final EstagiariosRepository repositoryEstagiarios;
    private  final RegistroRepository repositoryRegistros;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String,Object> redis;
    private final PasswordEncoder encoder;


    public EstagiariosService(EstagiariosRepository repositoryEstagiarios,
                              RegistroRepository repositoryRegistros,
                              PasswordEncoder encoder,
                              AuthenticationManager authManager,
                              JwtService jwtService,
                              RedisTemplate<String,Object> redis,
                              ObjectMapper objectMapper
                              ) {
        this.repositoryEstagiarios = repositoryEstagiarios;
        this.repositoryRegistros = repositoryRegistros;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.redis = redis;
        this.objectMapper = objectMapper;
    }

    public EstagiarioJSON estagiarioJson(Estagiario estagiario){
        return new EstagiarioJSON(estagiario.getMatricula(),estagiario.getUsuario());
    }

    public EstagiarioJSON findByMatricula(String matricula){
      return this.estagiarioJson(repositoryEstagiarios.findByMatricula(matricula));
    }

    public void create(RegistryRequestDTO dto){
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
          authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.username(),
                            dto.password()
                    )
            );
     var estagiario = repositoryEstagiarios.findByUsername(dto.username()).orElseThrow();
        return new Token(jwtService.generateToken(estagiario),Instant.now().minus(Duration.ofMinutes(1L)).toString());
    }

    public String openCheckIn(Setor dto){
        var checkIn = new CheckInRaw(
               SecurityContextHolder.getContext().getAuthentication().getName(),
                Instant.now().toString(),
                UUID.randomUUID().toString(),
                dto
        );
        // Não Mude esses metodos de get e set no Redis pois ultilizando metodos primitivos de get e set ira quebrar
        redis.opsForHash().put(checkIn.username(),checkIn.username(),checkIn);
        redis.expireAt(checkIn.username(),Instant.now().plus(Duration.ofDays(1L)));
        return checkIn.id();
    }


    @Transactional
    public void closeCheckIn() {
      var checkIn = (CheckInRaw) redis.opsForHash().get(
              SecurityContextHolder.getContext().getAuthentication().getName(),
              SecurityContextHolder.getContext().getAuthentication().getName()
      );
        assert checkIn != null;
        var hrSaida = Instant.now();
       var registro = new Registro(
               checkIn.id(),
               repositoryEstagiarios.findByUsername(checkIn.username()).orElseThrow(),
               checkIn.setor(),
               StatusRegistro.ANALISE,
               Duration.between(Instant.parse(checkIn.createdAT()),hrSaida).toString(),
               checkIn.createdAT(),
               hrSaida.toString()
       );
       repositoryRegistros.save(registro);
        redis.opsForHash().delete(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
     //   Duration duration = Duration.between(Instant.parse(checkIn.createdAT()),Instant.now());
    //    System.out.println(duration.toMinutes());
    }
}
