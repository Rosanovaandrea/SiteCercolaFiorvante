package com.example.SiteCercolaFioravante.customer.services.impl;

import com.example.SiteCercolaFioravante.customer.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.example.SiteCercolaFioravante.customer.services.CustomerAuthenticationService;
import com.example.SiteCercolaFioravante.customer.CustomerDtoComplete;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class CustomerAuthenticationServiceImpl implements CustomerAuthenticationService {

    CustomerRepository repository;


    @Override
    public CustomerDtoSafe doEmailPasswordReset(String email) {
        return null;
    }

    @Override
    public CustomerDtoSafe doPasswordReset(String password) {
        return null;
    }

    @Override
    public CustomerDtoSafe doLogin(String email, String password) {
        try{

        Long id = repository.getCustomerIdFromEmail(email);

        if(id == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato");


        String passwordFromDB = repository.getCustomerPasswordFromEmail(email);

        if (BCrypt.checkpw(password, passwordFromDB))
            return repository.getCustomerSafe(email);
        else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"password non valida");

        }catch(DataAccessException ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "erore interno del server durante il login");
        }

    }

    @Override
    public CustomerDtoSafe doRegistration(CustomerDtoComplete customer) {
        return null;
    }
}
