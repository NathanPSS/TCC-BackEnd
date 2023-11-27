package com.hujb.app.usuarios.estagiarios.config.middleware;

import com.hujb.app.config.auth.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtMiddlewareEstagiario extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsServiceEstagiario;

    @Autowired
    public JwtMiddlewareEstagiario(JwtService jwtService,

                         @Qualifier("userDetailsServiceEstagiario")
                         UserDetailsService userDetailsServiceEstagiario
    ) {
        this.jwtService = jwtService;
        this.userDetailsServiceEstagiario = userDetailsServiceEstagiario;

    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String headerToken = request.getHeader("Authorization");
        if(headerToken == null){
            System.out.println("Midleware Est");
            filterChain.doFilter(request,response);
            return;
        };
        final String jwtToken = headerToken.substring(7);
        final String username = jwtService.extractUsername(jwtToken);
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsServiceEstagiario.loadUserByUsername(username);
            if(jwtService.isValidToken(jwtToken,userDetails)){
                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.contains("admin") || path.contains("preceptor") || path.contains("setor");
    }
}
