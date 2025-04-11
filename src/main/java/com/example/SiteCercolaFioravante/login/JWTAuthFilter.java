package com.example.SiteCercolaFioravante.login;

import com.example.SiteCercolaFioravante.customer.services.CustomerAuthenticationService;
import com.example.SiteCercolaFioravante.customer.services.CustomerService;
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


@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private static CustomerAuthenticationService userAuthProvider;

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
                SecurityContextHolder.getContext().setAuthentication(
                        userAuthProvider.doAuthentication(elements[1])
                );
            }catch(RuntimeException e){
                SecurityContextHolder.clearContext();
                throw e;
            }

        filterChain.doFilter(request,response);


    }
}
