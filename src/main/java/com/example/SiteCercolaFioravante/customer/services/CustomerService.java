package com.example.SiteCercolaFioravante.customer.services;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoEditAdmin;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoListProjection;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.reservation.Reservation;

import java.util.List;


public interface CustomerService {

    List<CustomerDtoListProjection> getCustomerByNameOrSurname(String nameSurname);

    CustomerDtoSafe getCustomerByPhoneNumber(String phoneNumber);

    long getCustomerIdFromEmail(String email);

    boolean insertCustomerFromAdmin(CustomerDtoSafe customer);

    boolean editCustomerFromAdmin(CustomerDtoEditAdmin customer);

    Customer getCustomerFromEmailReservation(String email);

    CustomerDtoSafe getCustomerFromEmail(String email);

    void inserReservationCustomer(Reservation reservation);
}

