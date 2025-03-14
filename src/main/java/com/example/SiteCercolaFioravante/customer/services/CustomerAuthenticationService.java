package com.example.SiteCercolaFioravante.customer.services;

import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoComplete;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import org.springframework.security.core.Authentication;

public interface CustomerAuthenticationService {
    boolean doEmailPasswordReset(String email );

    CustomerDtoSafe doPasswordReset(String password );

    String doLogin(String email, String password);

    String doRegistration(CustomerDtoComplete customer);

    Authentication doAuthentication(String token);

}
