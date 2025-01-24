package com.example.SiteCercolaFioravante.customer.services;

import com.example.SiteCercolaFioravante.customer.CustomerDtoComplete;
import com.example.SiteCercolaFioravante.customer.CustomerDtoSafe;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;

public interface CustomerAuthenticationService {
    CustomerDtoSafe doEmailPasswordReset(String email );

    CustomerDtoSafe doPasswordReset(String password );

    String doLogin(String email, String password);

    String doRegistration(CustomerDtoComplete customer);

    Authentication doAuthentication(String token);

}
