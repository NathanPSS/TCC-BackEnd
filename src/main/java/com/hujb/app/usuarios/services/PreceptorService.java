package com.hujb.app.usuarios.services;

import com.hujb.app.config.auth.jwt.JwtService;
import com.hujb.app.config.auth.AuthRequestBody;
import com.hujb.app.config.auth.jwt.Token;
import com.hujb.app.registros.dto.RegistryUI;
import com.hujb.app.registros.dto.RegistryRejected;
import com.hujb.app.registros.entities.Registro;
import com.hujb.app.registros.entities.RegistroAssinado;
import com.hujb.app.registros.entities.RegistroRejeitado;
import com.hujb.app.registros.repositories.RegistroAssinadoRepository;
import com.hujb.app.registros.repositories.RegistroRepository;
import com.hujb.app.registros.repositories.RegistryRejectedRepository;
import com.hujb.app.setores.SetorRepository;
import com.hujb.app.usuarios.entities.Usuario;
import com.hujb.app.usuarios.enums.Role;
import com.hujb.app.usuarios.preceptor.dto.CreatePreceptorDTO;
import com.hujb.app.usuarios.preceptor.dto.PreceptorSummary;
import com.hujb.app.usuarios.preceptor.entities.Preceptor;
import com.hujb.app.usuarios.preceptor.repositories.PreceptorRepository;
import com.hujb.app.utils.time.FormaterDateStrings;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for preceptor services
 * @author Nathan Pereira Sarmento
 */

@Service
public class PreceptorService {
    private final AuthenticationManager authenticationManagerPreceptor;
    private final PreceptorRepository repositoryPreceptor;
    private final RegistroRepository repositoryRegistros;
    private final RegistryRejectedRepository registryRejectedRepository;
    private final RegistroAssinadoRepository registroAssinadoRepository;
    private final SetorRepository repositorySetor;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;

    /**
     *
     * @param authenticationManagerPreceptor
     * @param repositoryPreceptor
     * @param repositoryRegistros
     * @param repositorySetor
     * @param registryRejectedRepository
     * @param registroAssinadoRepository
     * @param jwtService
     * @param encoder
     */

    public PreceptorService(

            AuthenticationManager authenticationManagerPreceptor,
            PreceptorRepository repositoryPreceptor,
            RegistroRepository repositoryRegistros,
            SetorRepository repositorySetor,
            RegistryRejectedRepository registryRejectedRepository,
            RegistroAssinadoRepository registroAssinadoRepository,
            JwtService jwtService,
            PasswordEncoder encoder
    ) {
        this.authenticationManagerPreceptor = authenticationManagerPreceptor;
        this.repositoryPreceptor = repositoryPreceptor;
        this.registryRejectedRepository = registryRejectedRepository;
        this.jwtService = jwtService;
        this.encoder = encoder;
        this.repositorySetor = repositorySetor;
        this.repositoryRegistros = repositoryRegistros;
        this.registroAssinadoRepository = registroAssinadoRepository;
    }

    /**
     * Method for provide a JWT Token for Preceptor Entity
     * @param dto
     * @return <code>{@link Token}</code>
     */
    public Token authenticate(AuthRequestBody dto)  {
       try {
           var preceptor = repositoryPreceptor.findByUsername(dto.username());
           if(preceptor.isEmpty()){
               return null;
           }
           authenticationManagerPreceptor.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           dto.username(),
                           dto.password()
                   )
           );

           // token is 2 minutes more valid than the information of his expires, this prevent the token expire between API comunication
           var token = new Token(jwtService.generateToken(preceptor.get()),Instant.now().minus(Duration.ofMinutes(2L)).plus(Duration.ofDays(1L)).toString());
           System.out.println(token.token());
           return token;
       } catch (InternalAuthenticationServiceException e){
           System.out.println(e.toString());
       }
        return null;
    }

    public void create(CreatePreceptorDTO dto) {
        var setor = repositorySetor.findById(dto.idSetor()).get();
        repositoryPreceptor.save(
                new Preceptor(
                        new Usuario(
                                dto.nome(),
                                Role.USER
                        ),
                        dto.matricula(),
                        setor,
                        encoder.encode(dto.password())
                ));
    }
   public void update(String matricula,String nome,Long idSetor,Long usuarioId){
       repositoryPreceptor.save(
             new Preceptor(
                     new Usuario(
                             usuarioId,
                             nome
                     ),
                     matricula,
                     repositorySetor.findById(idSetor).orElseThrow()
             )
        );
   }
   public void updatePassword(String matricula,String password){
        repositoryPreceptor.save(
                new Preceptor(
                        matricula,
                        encoder.encode(password)
                )
        );
   }

    @Transactional
    public void remove(String matricula){
        repositoryPreceptor.softDeleteByMatricula(matricula);
    }

    public PreceptorSummary findSummaryByMatricula(String matricula){
       return repositoryPreceptor.findSummaryByMatricula(matricula).orElseThrow();
    }

    public List<RegistryUI> findAllRegistries() throws SQLException {
        var rows = repositoryRegistros.findAllRegistriesForSing(SecurityContextHolder.getContext().getAuthentication().getName());
        var result = new LinkedList<RegistryUI>();
        for (Tuple tuple : rows) {
            result.add(new RegistryUI(
                    tuple.get("id",String.class),
                    tuple.get("nome", String.class),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy").format(Instant.parse(tuple.get("hr_saida", String.class)).atZone(ZoneId.systemDefault())),
                    DateTimeFormatter.ofPattern("HH:mm:ss").format(Instant.parse(tuple.get("hr_entrada", String.class)).atZone(ZoneId.systemDefault())),
                    DateTimeFormatter.ofPattern("HH:mm:ss").format(Instant.parse(tuple.get("hr_saida", String.class)).atZone(ZoneId.systemDefault())),
                    FormaterDateStrings.parseTimeDuration(Duration.parse(tuple.get("tempo", String.class))),
                    tuple.get("descricao", String.class)
            ));
        }
        return result;
    }

    public void saveRejectedRegistry(RegistryRejected dto){

         var rejectedRegistry = new RegistroRejeitado(
                 repositoryRegistros.findById(dto.idRegistry()).orElseThrow(),
                 repositoryPreceptor.findByUsername(
                         SecurityContextHolder.getContext().getAuthentication().getName()
                         ).orElseThrow(),
                 dto.menssage(),
                 dto.reason()
         );
         registryRejectedRepository.save(rejectedRegistry);
    }

    public void saveSignedRegistries(){
        var registriesForSign = repositoryRegistros.findAllRegistriesForAnalisys(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        var preceptor = repositoryPreceptor.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow();
       var registriesSigned = new LinkedList<RegistroAssinado>();
        for (Registro registro : registriesForSign) {
            registriesSigned.add(
                    new RegistroAssinado(registro,preceptor)
            );
        }
        registroAssinadoRepository.saveAll(registriesSigned);
    }
    }

