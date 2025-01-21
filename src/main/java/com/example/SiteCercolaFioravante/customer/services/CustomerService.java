package com.example.SiteCercolaFioravante.customer.services;

import java.util.List;


public interface CustomerService {

    List<CustomerProjectionList> getCustomerByNameOrSurname(String nameSurname);

    CustomerProjectionSingle getCustomerByPhoneNumber(long phoneNumber);

    List<CustomerProjectionList> getCustomerByEmail(String email);

    long getCustomerIdFromEmail(String email);
}

