package com.example.SiteCercolaFioravante.customer.services;


import org.springframework.security.core.Authentication;

public interface CustomerAuthenticationService {
    boolean doEmailPasswordReset(String email );

    boolean doPasswordReset( String token, String password);

    String[] doLogin(String email, String password);

    String doRefreshAccessToken(String token);

    Authentication doAuthentication(String token);

    boolean doLogout(String refreshToken);

}
