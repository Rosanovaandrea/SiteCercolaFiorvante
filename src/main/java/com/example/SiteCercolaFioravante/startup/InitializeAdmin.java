package com.example.SiteCercolaFioravante.startup;

import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.services.CustomerService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InitializeAdmin {
    private final CustomerService customerService;
    private final String name;
    private final String surname;
    private final String email;
    private final String password;
    private final String number;

    public InitializeAdmin(@Autowired CustomerService customerService,
                           @Value("${admin.name}") String name,
                           @Value("${admin.surname}")String surname,
                           @Value("${admin.email}")String email,
                           @Value("${admin.password}")String password,
                           @Value("${admin.number}")String number) {
        this.customerService = customerService;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.number = number;
    }

    @PostConstruct
    public void initAdmin(){
        if(customerService.isAdminPresent()) return;
        CustomerDtoSafe customerDtoSafe = new CustomerDtoSafe(name,surname,email,number);
        customerService.insertAdmin(customerDtoSafe,password);
    }
}
