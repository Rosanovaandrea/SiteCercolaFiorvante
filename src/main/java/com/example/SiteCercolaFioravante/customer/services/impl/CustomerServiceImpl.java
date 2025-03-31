package com.example.SiteCercolaFioravante.customer.services.impl;

import com.example.SiteCercolaFioravante.customer.*;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoEditAdmin;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoList;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.MapperCustomer;
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

    private final CustomerRepository repository;
    private final MapperCustomer mapper;

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
    public boolean insertCustomerFromAdmin(CustomerDtoSafe customer) {


        //TO-DO make a function that delete in loco customer added for remote registration

        //random generated password for in loco added Customer
        long id = repository.getCurrentId();
        Customer customerDB = new Customer();
        customerDB.setEmail((id+1)+"customer@gmail.com");

        //random generated password for in loco added Customer
        byte[] code = new byte[10];
        Random codeGenerator = new Random();
        codeGenerator.nextBytes(code);
        String str = new String(code, StandardCharsets.UTF_8);
        str=BCrypt.hashpw(str, BCrypt.gensalt());
        customerDB.setPassword(str);

        mapper.fromDtoSafeToCustomer(customer,customerDB);

        repository.save(customerDB);
        repository.flush();

        return true;
    }

    @Override
    public boolean editCustomerFromAdmin(CustomerDtoEditAdmin customer) {
        Customer customerDB = repository.getCustomerFromEmail(customer.prevEmail());
        mapper.fromDtoEditAdminToCustomer(customer,customerDB);
        repository.save(customerDB);
        repository.flush();
        return true;
    }

    @Override
    public Customer getCustomerFromEmail(String email) {
      return  repository.getCustomerFromEmail(email);
    }
}
