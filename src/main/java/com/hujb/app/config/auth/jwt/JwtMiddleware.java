package com.hujb.app.config.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtMiddleware extends OncePerRequestFilter {

    private JwtService jwtService;

    private UserDetailsService userDetailsService;

    @Autowired
    public JwtMiddleware(JwtService jwtService,UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
    final String headerToken = request.getHeader("Authorization");
    if(headerToken == null){
        filterChain.doFilter(request,response);
        return;
    };
    final String jwtToken = headerToken.substring(7);
    final String username = jwtService.extractUsername(jwtToken);
    if( username != null && SecurityContextHolder.getContext().getAuthentication() == null){
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
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
}
