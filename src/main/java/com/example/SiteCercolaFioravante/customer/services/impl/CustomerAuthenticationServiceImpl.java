package com.example.SiteCercolaFioravante.customer.services.impl;

import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.example.SiteCercolaFioravante.customer.services.CustomerAuthenticationService;
import com.example.SiteCercolaFioravante.customer.CustomerDtoComplete;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
        long id = repository.getCustomerIdFromEmail(email);

        if(id != 0L) System.out.println("error");

        String passwordFromDB = repository.getCustomerPasswordFromEmail(email);

        BCrypt.checkpw(password,passwordFromDB);

    }

    @Override
    public CustomerDtoComplete doRegistration(CustomerDtoComplete customer) {
        return null;
    }
}
