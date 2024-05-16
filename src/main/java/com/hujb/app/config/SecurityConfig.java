package com.hujb.app.config;

import com.hujb.app.usuarios.admin.config.middleware.JwtAdminMiddleware;
import com.hujb.app.usuarios.estagiarios.config.middleware.JwtMiddlewareEstagiario;
import com.hujb.app.usuarios.preceptor.config.middleware.JwtMiddlewarePreceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private final JwtMiddlewarePreceptor jwtMiddlewarePreceptor;

    private final AuthenticationProvider authenticationProviderPreceptor;

    private final JwtMiddlewareEstagiario jwtMiddlewareEstagiario;

    private final AuthenticationProvider authenticationProviderEstagiaro;

    private final JwtAdminMiddleware jwtAdminMiddleware;

    private final AuthenticationProvider authenticationProviderAdmin;

    public SecurityConfig(
            JwtMiddlewarePreceptor jwtMiddlewarePreceptor,

            @Qualifier("authenticationProviderPreceptor")
            AuthenticationProvider authenticationProviderPreceptor,

            JwtMiddlewareEstagiario jwtMiddlewareEstagiario,

            @Qualifier("authenticationProviderEstagiario")
            AuthenticationProvider authenticationProviderEstagiaro,

            @Qualifier("authenticationProviderAdmin")
            AuthenticationProvider authenticationProviderAdmin,

            JwtAdminMiddleware jwtAdminMiddleware

    ) {
        this.authenticationProviderPreceptor = authenticationProviderPreceptor;
        this.jwtMiddlewarePreceptor = jwtMiddlewarePreceptor;
        this.authenticationProviderEstagiaro = authenticationProviderEstagiaro;
        this.jwtMiddlewareEstagiario = jwtMiddlewareEstagiario;
        this.jwtAdminMiddleware = jwtAdminMiddleware;
        this.authenticationProviderAdmin = authenticationProviderAdmin;
    }



    @Bean
    public SecurityFilterChain securityFilterChainEstagiario(HttpSecurity http) throws Exception {
        http
                .securityMatcher("estagiario/**")
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("estagiario/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProviderEstagiaro)
                .addFilterAfter(jwtMiddlewareEstagiario, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    };
    @Bean
    public SecurityFilterChain securityFilterChainAdmin(HttpSecurity http) throws Exception {
        http
                .securityMatcher("admin/**")
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("admin/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .authenticationProvider(authenticationProviderAdmin)
                .addFilterAfter(jwtAdminMiddleware, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    };

    @Bean
    public SecurityFilterChain securityFilterChainPreceptor(HttpSecurity http) throws Exception {
        http
                .securityMatcher("preceptor/**")
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("preceptor/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .authenticationProvider(authenticationProviderPreceptor)
                .addFilterBefore(jwtMiddlewarePreceptor, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    };
}
