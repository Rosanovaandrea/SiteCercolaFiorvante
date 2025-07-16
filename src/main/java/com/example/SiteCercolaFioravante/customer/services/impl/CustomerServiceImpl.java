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
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
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

    @Override
    @Transactional
    public boolean insertAdmin(CustomerDtoSafe customer,String password) {

        Customer customerDB = mapper.fromDtoSafeToCustomer(customer);
        customerDB.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        customerDB.setRole(CustomerRole.ADMIN);
        repository.save(customerDB);
        repository.flush();

        return true;
    }



    @Transactional
    @Override
    public boolean editCustomerFromAdmin(CustomerDtoEditAdmin customer) {
        Customer customerDB = repository.findCustomerByEmail(customer.prevEmail()).orElse(null);
        if(customerDB == null) throw new IllegalArgumentException("utente non esistente");
        if(customerDB.getRole() == CustomerRole.ADMIN) throw new IllegalArgumentException("non puoi modificare un admin");
        mapper.fromDtoEditAdminToCustomer(customer,customerDB);
        repository.save(customerDB);
        repository.flush();
        return true;
    }


    @Override
    public Customer getCustomerFromEmailReservation(String email) {
        Customer customer = repository.findCustomerByEmail(email).orElse(null);
        if(customer == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"cliente non valido");
        if(customer.getRole() == CustomerRole.ADMIN) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"non puoi inserire una prenotazione su di un admin");
        return  customer;
    }

    @Override
    public boolean isAdminPresent() {
        Optional<Customer> customerOpt= repository.findCustomerByRole(CustomerRole.ADMIN);
        return customerOpt.isPresent();
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
