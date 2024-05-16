package com.hujb.app.usuarios.admin.services;

import com.hujb.app.config.auth.AuthRequestBody;
import com.hujb.app.config.auth.jwt.JwtService;
import com.hujb.app.config.auth.jwt.Token;
import com.hujb.app.usuarios.admin.dto.AdminSummary;
import com.hujb.app.usuarios.admin.dto.CreateAdminDTO;
import com.hujb.app.usuarios.admin.entities.Administracao;
import com.hujb.app.usuarios.admin.repositories.AdminRepository;
import com.hujb.app.usuarios.entities.Usuario;
import com.hujb.app.usuarios.enums.Role;
import com.hujb.app.usuarios.estagiarios.repositories.UsuariosRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    private final AuthenticationManager authenticationManagerAdmin;

    private  final UsuariosRepository usuariosRepository;

    private final JwtService jwtService;

    private final RedisTemplate<String,Object> redis;

    private final PasswordEncoder encoder;
    public AdminService(AdminRepository adminRepository,

                        AuthenticationManager authenticationManagerAdmin,
                        UsuariosRepository usuariosRepository, JwtService jwtService, RedisTemplate<String, Object> redis, PasswordEncoder encoder) {
        this.adminRepository = adminRepository;
        this.authenticationManagerAdmin = authenticationManagerAdmin;
        this.usuariosRepository = usuariosRepository;
        this.jwtService = jwtService;
        this.redis = redis;
        this.encoder = encoder;
    }

    public Token authenticate(AuthRequestBody dto) {

        var admin = adminRepository.findByUsername(dto.username()).orElseThrow(() -> new UsernameNotFoundException("Admin não Encontrado"));
        authenticationManagerAdmin.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.username(),
                        dto.password()
                )
        );

        // token is 2 minutes more valid than the information of his expires, this prevent the token expire between API comunication
        var token = new Token(jwtService.generateToken(admin), Instant.now().minus(Duration.ofMinutes(2L)).plus(Duration.ofDays(1L)).toString());
        System.out.println(token.token());
        return token;
    }

    public void create(CreateAdminDTO dto){
        var admin = new Administracao(
                new Usuario(
                        dto.nome(),
                        Role.ADMIN
                ),
                dto.matricula(),
                encoder.encode(dto.password())
        );
        adminRepository.save(admin);
    };

    public void update(String matricula,String nome,Long usuarioId){
        adminRepository.save(
                new Administracao(
                        new Usuario(
                                usuarioId,
                                nome
                        ),
                        matricula
                )
        );
    }

    public void updatePassword(String matricula,String password){
        adminRepository.save(new Administracao(matricula,encoder.encode(password)));
    }

    public AdminSummary findAdmin(String matricula){
        return adminRepository.findSummaryByMatricula(matricula).orElseThrow();
    }

@Transactional
public void remove(String matricula){
    System.out.println(matricula);
    adminRepository.softDeleteByMatricula(matricula);
}
}
