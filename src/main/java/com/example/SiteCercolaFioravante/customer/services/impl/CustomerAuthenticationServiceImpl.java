package com.example.SiteCercolaFioravante.customer.services.impl;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.example.SiteCercolaFioravante.customer.services.CustomerAuthenticationService;
import com.example.SiteCercolaFioravante.customer.CustomerDtoComplete;
import com.example.SiteCercolaFioravante.utils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerAuthenticationServiceImpl implements CustomerAuthenticationService {

    private final JwtUtils jwtUtils;
    private final CustomerRepository repository;


    @Override
    public CustomerDtoSafe doEmailPasswordReset(String email) {
        return null;
    }

    @Override
    public CustomerDtoSafe doPasswordReset(String password) {
        return null;
    }

    @Override
    public String doLogin(String email, String password) {
        try{

        Long id = repository.getCustomerIdFromEmail(email);

        if(id == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato");


        String passwordFromDB = repository.getCustomerPasswordFromEmail(email);

        if (BCrypt.checkpw(password, passwordFromDB))
            return jwtUtils.createToken(email);
        else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"password non valida");

        } catch(DataAccessException ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "errore interno del database durante il login");
        }catch (Exception ex){
            throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "errore generico interno del sistema durante  il login");
        }

    }

    @Override
    public String doRegistration(CustomerDtoComplete customer) {
        try {
            Customer customerDB = new Customer();

            customerDB.setEmail(customer.email());
            customerDB.setName(customer.name());
            customerDB.setSurname(customer.surname());
            customerDB.setRole(customer.role());
            customerDB.setPhoneNumber(customer.phoneNumber());

            String password = BCrypt.hashpw(customer.password(), BCrypt.gensalt());

            customerDB.setPassword(password);

            repository.save(customerDB);
            repository.flush();

          return  jwtUtils.createToken(customer.email());


        }catch(DataAccessException ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "erore interno del database durante il login");
        }catch (Exception ex){
            throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "errore generico interno del sistema durante  il login");
        }


    }

    @Override
    public Authentication doAuthentication(String token) {
        String email = jwtUtils.getTokenEmail(token);
        Date expiration = jwtUtils.getTokenDate(token);
        CustomerDtoSafe customer = repository.getCustomerSafe(email);
        Date now = new Date();
        List<SimpleGrantedAuthority> authorities = new LinkedList<SimpleGrantedAuthority>();
        String role = customer.role();
        authorities.add(new SimpleGrantedAuthority(role));
        Authentication authentication = new UsernamePasswordAuthenticationToken(customer,token, authorities);
        return authentication;
    }
}
