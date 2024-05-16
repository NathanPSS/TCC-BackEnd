package com.hujb.app.usuarios.admin.config;

import com.hujb.app.usuarios.admin.repositories.AdminRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfigAdministracao {


    private final AdminRepository adminRepository;

    public ApplicationConfigAdministracao(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Bean
    public UserDetailsService userDetailsServiceAdmin(){
        return username -> adminRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("Admin nao encontrado"));
    }

    @Bean
    public AuthenticationProvider authenticationProviderAdmin(
            PasswordEncoder encoder
    ){
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsServiceAdmin());
        authenticationProvider.setPasswordEncoder(encoder);
        return authenticationProvider;
    }
}
