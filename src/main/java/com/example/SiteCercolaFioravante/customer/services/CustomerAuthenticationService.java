package com.example.SiteCercolaFioravante.customer.services;

public interface CustomerAuthenticationService {
    CustomerDtoComplete doEmailPasswordReset( String email );

    CustomerDtoComplete doPasswordReset( String password );

    CustomerDtoComplete doLogin(String email, String password);

    CustomerDtoComplete doRegistration(CustomerDtoComplete customer);

}
