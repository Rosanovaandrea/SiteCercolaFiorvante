package com.example.SiteCercolaFioravante.customer.services.impl;

import com.example.SiteCercolaFioravante.customer.CustomerDtoList;
import com.example.SiteCercolaFioravante.customer.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.example.SiteCercolaFioravante.customer.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository repository;
    @Override
    public List<CustomerDtoList> getCustomerByNameOrSurname(String nameSurname) {
        return repository.getCustomerByNameOrSurname(nameSurname);
    }

    @Override
    public CustomerDtoSafe getCustomerByPhoneNumber(long phoneNumber) {
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
}
