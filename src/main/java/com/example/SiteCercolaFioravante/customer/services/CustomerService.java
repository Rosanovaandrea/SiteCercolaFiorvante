package com.example.SiteCercolaFioravante.customer.services;

import com.example.SiteCercolaFioravante.customer.CustomerDtoList;
import com.example.SiteCercolaFioravante.customer.CustomerDtoSafe;

import java.util.List;


public interface CustomerService {

    List<CustomerDtoList> getCustomerByNameOrSurname(String nameSurname);

    CustomerDtoSafe getCustomerByPhoneNumber(long phoneNumber);

    List<CustomerDtoList> getCustomerByEmail(String email);

    long getCustomerIdFromEmail(String email);
}

