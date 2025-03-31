package com.example.SiteCercolaFioravante.customer.services.impl;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.CustomerRole;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.MapperCustomer;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.example.SiteCercolaFioravante.customer.services.CustomerAuthenticationService;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoComplete;
import com.example.SiteCercolaFioravante.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomerAuthenticationServiceImpl implements CustomerAuthenticationService {

    private final JwtUtils jwtUtils;
    private final CustomerRepository repository;
    private final MapperCustomer mapper;


    @Override
    public boolean doEmailPasswordReset(String email) {
        byte[] code = new byte[4];
        Random codeGenerator = new Random();
        codeGenerator.nextBytes(code);
        String tokenString = jwtUtils.createToken(Arrays.toString(code));
        repository.setToken(tokenString,email);
        return true;
    }

    @Override
    public CustomerDtoSafe doPasswordReset(String password) {
        return null;
    }

    @Override
    public String doLogin(String email, String password) {


        Long id = repository.getCustomerIdFromEmail(email);

        if(id == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato");


        String passwordFromDB = repository.getCustomerPasswordFromEmail(email);

        if (BCrypt.checkpw(password, passwordFromDB))
            return jwtUtils.createToken(email);
        else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"password non valida");

    }

    @Override
    public String doRegistration(CustomerDtoComplete customer) {

            Customer customerDB = new Customer();
            mapper.fromDtoCompleteToCustomer(customer,customerDB);
            customerDB.setRole(CustomerRole.CUSTOMER);

            String password = BCrypt.hashpw(customer.password(), BCrypt.gensalt());

            customerDB.setPassword(password);

            repository.save(customerDB);
            repository.flush();

          return  jwtUtils.createToken(customer.email());

    }

    @Override
    public Authentication doAuthentication(String token) {
        String email = jwtUtils.getTokenEmail(token);
        LocalDateTime expiration = jwtUtils.getTokenLocalDate(token);
        Customer customer = repository.getCustomerFromEmail( email);
        LocalDateTime now =  LocalDateTime.now();
        List<SimpleGrantedAuthority> authorities = new LinkedList<SimpleGrantedAuthority>();
        String role = CustomerRole.CUSTOMER.toString();
        authorities.add(new SimpleGrantedAuthority(role));
        return new UsernamePasswordAuthenticationToken(customer,token, authorities);
    }
}
