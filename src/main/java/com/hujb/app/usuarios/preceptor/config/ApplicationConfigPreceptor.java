package com.hujb.app.usuarios.preceptor.config;


import com.hujb.app.usuarios.preceptor.repositories.PreceptorRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfigPreceptor {
    private final PreceptorRepository preceptorRepository;

    public ApplicationConfigPreceptor(
         PreceptorRepository preceptorRepository
    ) {
        this.preceptorRepository = preceptorRepository;

    }

    @Bean
    public UserDetailsService userDetailsServicePreceptor(){
        return username -> preceptorRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Preceptor nao encontraod"));
    }

    @Bean
    public AuthenticationProvider authenticationProviderPreceptor(
            PasswordEncoder encoder
    ){
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsServicePreceptor());
        authenticationProvider.setPasswordEncoder(encoder);
        return authenticationProvider;
    }

}
