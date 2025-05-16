package com.example.SiteCercolaFioravante.customer.services.impl;

import com.example.SiteCercolaFioravante.customer.*;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoEditAdmin;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoListProjection;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.MapperCustomer;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.example.SiteCercolaFioravante.customer.services.CustomerService;
import com.example.SiteCercolaFioravante.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final MapperCustomer mapper;

    @Override
    public List<CustomerDtoListProjection> getCustomerByNameOrSurname(String nameSurname) {
        return repository.getCustomerByNameOrSurname(nameSurname);
    }

    @Override
    public CustomerDtoSafe getCustomerByPhoneNumber(String phoneNumber) {
        return repository.getCustomerByPhoneNumber(phoneNumber);
    }

    @Override
    public long getCustomerIdFromEmail(String email) {
        return repository.getCustomerIdFromEmail(email);
    }

    @Transactional
    @Override
    public boolean insertCustomerFromAdmin(CustomerDtoSafe customer) {


        //TO-DO make a function that delete in loco customer added for remote registration


        Customer customerDB = new Customer();
        customerDB = mapper.fromDtoSafeToCustomer(customer);

        long id = 0;
        Optional<Long> opt = repository.getCurrentId();

        if(opt.isPresent()) id = opt.get();
        customerDB.setEmail((id+1)+"customer@gmail.com");

        //random generated password for in loco added Customer
        byte[] code = new byte[10];
        Random codeGenerator = new Random();
        codeGenerator.nextBytes(code);
        String str = new String(code, StandardCharsets.UTF_8);
        str=BCrypt.hashpw(str, BCrypt.gensalt());
        customerDB.setPassword(str);


        customerDB.setRole(CustomerRole.CUSTOMER_IN_LOCO);

        repository.save(customerDB);
        repository.flush();

        return true;
    }

    @Transactional
    @Override
    public boolean editCustomerFromAdmin(CustomerDtoEditAdmin customer) {
        Customer customerDB = repository.findCustomerByEmail(customer.prevEmail()).orElse(null);
        mapper.fromDtoEditAdminToCustomer(customer,customerDB);
        repository.save(customerDB);
        repository.flush();
        return true;
    }


    @Override
    public Customer getCustomerFromEmailReservation(String email) {
      return  repository.findCustomerByEmail(email).orElse(null);
    }

    @Override
    public CustomerDtoSafe getCustomerFromEmail(String email) {
        return repository.findCustomerDtoSafeByEmail(email).orElse(null);
    }

    @Override
    public void inserReservationCustomer(Reservation reservation) {

        Customer customerDb = reservation.getCustomer();

        if(customerDb.getReservations() == null)
            customerDb.setReservations(new LinkedList<Reservation>());

        customerDb.getReservations().add(reservation);
        repository.saveAndFlush(customerDb);

    }
}
