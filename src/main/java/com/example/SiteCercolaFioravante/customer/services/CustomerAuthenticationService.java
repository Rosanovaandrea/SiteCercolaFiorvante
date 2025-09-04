package com.example.SiteCercolaFioravante.customer.services;

import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoComplete;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface CustomerAuthenticationService {
    boolean doEmailPasswordReset(String email );

    boolean doPasswordReset( String token, String password);

    String[] doLogin(String email, String password);

    String[] doRegistration(CustomerDtoComplete customer);

    String doRefreshAccessToken(String token);

    Authentication doAuthentication(String token);

    boolean doLogout(String refreshToken);

}
