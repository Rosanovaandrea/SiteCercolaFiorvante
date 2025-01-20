package com.example.SiteCercolaFioravante.customer.services.impl;

import com.example.SiteCercolaFioravante.customer.repository.CustomerProjectionList;
import com.example.SiteCercolaFioravante.customer.repository.CustomerProjectionSingle;
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
    public List<CustomerProjectionList> getCustomerByNameOrSurname(String nameSurname) {
        return repository.getCustomerByNameOrSurname(nameSurname);
    }

    @Override
    public CustomerProjectionSingle getCustomerByPhoneNumber(long phoneNumber) {
        return repository.getCustomerByPhoneNumber(phoneNumber);
    }

    @Override
    public List<CustomerProjectionList> getCustomerByEmail(String email) {
        return repository.getCustomerByEmail(email);
    }

    @Override
    public long getCustomerIdFromEmail(String email) {
        return repository.getCustomerIdFromEmail(email);
    }
}
