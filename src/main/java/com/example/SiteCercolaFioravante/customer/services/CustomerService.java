package com.example.SiteCercolaFioravante.customer.services;

import com.example.SiteCercolaFioravante.customer.repository.CustomerProjectionList;
import com.example.SiteCercolaFioravante.customer.repository.CustomerProjectionSingle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CustomerService {

    List<CustomerProjectionList> getCustomerByNameOrSurname(String nameSurname);

    CustomerProjectionSingle getCustomerByPhoneNumber(long phoneNumber);

    List<CustomerProjectionList> getCustomerByEmail(String email);

    long getCustomerIdFromEmail(String email);
}

