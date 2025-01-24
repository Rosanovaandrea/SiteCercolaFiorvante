package com.example.SiteCercolaFioravante.login;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private UserAuthProvider userAuthProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);

            if( header == null ) filterChain.doFilter(request,response);

            String[] elements = header.split( " " );

            if(!(elements.length == 2) || !elements[0].equals("Bearer"))
                filterChain.doFilter(request,response);

            try{
                if(SecurityContextHolder.getContext() != null){
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    authentication
                }
                /*SecurityContextHolder.getContext().setAuthentication(
                     userAuthProvider.validateToken(elements[1])
                );*/
            }catch(RuntimeException e){
                SecurityContextHolder.clearContext();
                throw e;
            }

        filterChain.doFilter(request,response);


    }
}
