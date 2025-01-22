package com.example.SiteCercolaFioravante.customer.services;

import com.example.SiteCercolaFioravante.customer.CustomerDtoComplete;
import com.example.SiteCercolaFioravante.customer.CustomerDtoSafe;

public interface CustomerAuthenticationService {
    CustomerDtoSafe doEmailPasswordReset(String email );

    CustomerDtoSafe doPasswordReset(String password );

    CustomerDtoSafe doLogin(String email, String password);

    CustomerDtoSafe doRegistration(CustomerDtoComplete customer);

}
