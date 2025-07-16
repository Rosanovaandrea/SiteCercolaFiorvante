package com.example.SiteCercolaFioravante.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.SiteCercolaFioravante.customer.services.CustomerAuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;


public class JWTAuthFilter extends OncePerRequestFilter {

    private final CustomerAuthenticationService userAuthProvider;
    private final HandlerExceptionResolver resolver;
    private final static String BEARER = "Bearer";


    public JWTAuthFilter(CustomerAuthenticationService userAuthProvider, HandlerExceptionResolver resolver){
        this.userAuthProvider = userAuthProvider;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal( HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain )
            throws ServletException, IOException {

        try{
            String header = request.getHeader( HttpHeaders.AUTHORIZATION );

            String[] elements = header.split( " " );
            String token = null;

            if(elements[0].equals( BEARER )) {
                token = elements[1];
            }

            SecurityContextHolder.getContext().setAuthentication( userAuthProvider.doAuthentication( token ) );

            } catch ( JWTVerificationException e ) {
                SecurityContextHolder.clearContext();
                resolver.resolveException(request, response, null, e);
            }catch( Exception  e){
                SecurityContextHolder.clearContext();
                resolver.resolveException(request, response, null, new ResponseStatusException( HttpStatus.UNAUTHORIZED,"richiesta non autorizzata" ) );
            }
    }
}
