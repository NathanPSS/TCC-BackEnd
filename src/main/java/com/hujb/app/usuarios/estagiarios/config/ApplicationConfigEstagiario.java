package com.hujb.app.usuarios.estagiarios.config;

import com.hujb.app.usuarios.estagiarios.repositories.EstagiariosRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfigEstagiario {
    private final EstagiariosRepository estagiariosRepository;

    public ApplicationConfigEstagiario(
            EstagiariosRepository estagiariosRepository
    ) {
        this.estagiariosRepository = estagiariosRepository;

    }

    @Bean
    public UserDetailsService userDetailsServiceEstagiario(){
        return username -> estagiariosRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("Estagiario nao encotrado"));
    }


    @Bean
    public AuthenticationProvider authenticationProviderEstagiario(
            PasswordEncoder encoder
    ){
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsServiceEstagiario());
        authenticationProvider.setPasswordEncoder(encoder);
        return authenticationProvider;
    }
}
