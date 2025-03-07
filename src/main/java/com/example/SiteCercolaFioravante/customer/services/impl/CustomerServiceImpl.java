package com.example.SiteCercolaFioravante.customer.services.impl;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.CustomerDtoComplete;
import com.example.SiteCercolaFioravante.customer.CustomerDtoList;
import com.example.SiteCercolaFioravante.customer.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.example.SiteCercolaFioravante.customer.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository repository;
    @Override
    public List<CustomerDtoList> getCustomerByNameOrSurname(String nameSurname) {
        return repository.getCustomerByNameOrSurname(nameSurname);
    }

    @Override
    public CustomerDtoSafe getCustomerByPhoneNumber(String phoneNumber) {
        return repository.getCustomerByPhoneNumber(phoneNumber);
    }

    @Override
    public List<CustomerDtoList> getCustomerByEmail(String email) {
        return repository.getCustomerByEmail(email);
    }

    @Override
    public long getCustomerIdFromEmail(String email) {
        return repository.getCustomerIdFromEmail(email);
    }

    @Override
    public boolean insertCustomer(CustomerDtoSafe customer) {


        //TO-DO make a function that delete in loco customer added for remote registration

        //random generated password for in loco added Customer
        long id = repository.getCurrentId();
        Customer customerDB = new Customer();
        customerDB.setEmail((id+1)+"customer@gmail.com");

        //random generated password for in loco added Customer
        byte[] code = new byte[8];
        Random codeGenerator = new Random();
        codeGenerator.nextBytes(code);
        String str = new String(code, StandardCharsets.UTF_8);
        str=BCrypt.hashpw(str, BCrypt.gensalt());
        customerDB.setPassword(str);

        customerDB.setName(customer.name());
        customerDB.setSurname(customer.surname());
        customerDB.setRole("Customer from Admin");
        customerDB.setPhoneNumber(customer.phoneNumber());

        repository.save(customerDB);
        repository.flush();

        return true;
    }
}
