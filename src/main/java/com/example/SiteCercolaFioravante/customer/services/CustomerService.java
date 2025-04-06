package com.example.SiteCercolaFioravante.customer.services;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoEditAdmin;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoList;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;

import java.util.List;


public interface CustomerService {

    List<CustomerDtoList> getCustomerByNameOrSurname(String nameSurname);

    CustomerDtoSafe getCustomerByPhoneNumber(String phoneNumber);

    List<CustomerDtoList> getCustomerByEmail(String email);

    long getCustomerIdFromEmail(String email);

    boolean insertCustomerFromAdmin(CustomerDtoSafe customer);

    boolean editCustomerFromAdmin(CustomerDtoEditAdmin customer);

    Customer getCustomerFromEmailReservation(String email);

    CustomerDtoSafe getCustomerFromEmail(String email);
}

