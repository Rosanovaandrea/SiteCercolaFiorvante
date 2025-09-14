package com.example.SiteCercolaFioravante.customer.services;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoEditAdmin;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoListProjection;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerSafeProjection;
import com.example.SiteCercolaFioravante.reservation.Reservation;

import java.util.List;


public interface CustomerService {

    List<CustomerDtoListProjection> getCustomerByNameOrSurname(String nameSurname);


    boolean insertCustomerFromAdmin(CustomerDtoSafe customer);

    boolean editCustomerFromAdmin(CustomerDtoEditAdmin customer);

    boolean deleteCustomer(Long id);

    CustomerSafeProjection getCustomerFromID(Long Id);

    Customer getCustomerById(Long id);

}

