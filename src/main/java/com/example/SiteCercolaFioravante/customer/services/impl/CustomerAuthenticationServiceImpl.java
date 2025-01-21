package com.example.SiteCercolaFioravante.customer.services.impl;

import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.example.SiteCercolaFioravante.customer.services.CustomerAuthenticationService;
import com.example.SiteCercolaFioravante.customer.CustomerDtoComplete;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerAuthenticationServiceImpl implements CustomerAuthenticationService {

    CustomerRepository repository;


    @Override
    public CustomerDtoComplete doEmailPasswordReset(String email) {
        return null;
    }

    @Override
    public CustomerDtoComplete doPasswordReset(String password) {
        return null;
    }

    @Override
    public CustomerDtoComplete doLogin(String email, String password) {

    }

    @Override
    public CustomerDtoComplete doRegistration(CustomerDtoComplete customer) {
        return null;
    }
}
