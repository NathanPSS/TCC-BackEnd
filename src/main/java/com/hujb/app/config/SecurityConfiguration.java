package com.hujb.app.config;

import com.hujb.app.config.auth.jwt.JwtMiddleware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtMiddleware jwtMiddleware;

    private final AuthenticationProvider authenticationProvider;

    public SecurityConfiguration(JwtMiddleware jwtMiddleware,
                                 AuthenticationProvider authenticationProvider
    ) {
        this.jwtMiddleware = jwtMiddleware;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
              .csrf()
              .disable()
              .authorizeHttpRequests()
              .requestMatchers("/estagiario/**")
              .permitAll()
              .anyRequest()
              .authenticated()
              .and()
              .sessionManagement()
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
              .and()
              .authenticationProvider(authenticationProvider)
              .addFilterBefore(jwtMiddleware, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    };
}
