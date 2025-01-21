package com.example.SiteCercolaFioravante.customer.services;

import com.example.SiteCercolaFioravante.customer.CustomerDtoComplete;

public interface CustomerAuthenticationService {
    CustomerDtoComplete doEmailPasswordReset(String email );

    CustomerDtoComplete doPasswordReset( String password );

    CustomerDtoComplete doLogin(String email, String password);

    CustomerDtoComplete doRegistration(CustomerDtoComplete customer);

}
