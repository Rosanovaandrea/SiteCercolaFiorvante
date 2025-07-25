package com.example.SiteCercolaFioravante.customer.services.impl;

import com.example.SiteCercolaFioravante.customer.*;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.*;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.example.SiteCercolaFioravante.customer.services.CustomerService;
import com.example.SiteCercolaFioravante.reservation.Reservation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final MapperCustomer mapper;

    @Override
    public List<CustomerDtoListProjection> getCustomerByNameOrSurname(String nameSurname) {
        return repository.getCustomerByNameOrSurname(nameSurname);
    }



    @Transactional
    @Override
    public boolean insertCustomerFromAdmin(CustomerDtoSafe customer) {

        Customer customerDB = mapper.fromDtoSafeToCustomer(customer);

        customerDB.setRole(CustomerRole.CUSTOMER_IN_LOCO);

        try {
            repository.save(customerDB);
            repository.flush();
        }catch (ConstraintViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"numero di telefono già in uso");
        }

        return true;
    }





    @Transactional
    @Override
    public boolean editCustomerFromAdmin(CustomerDtoEditAdmin customer) {
        Customer customerDB = repository.findById(customer.id()).orElse(null);
        if(customerDB == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"utente non esistennte");
        if(customerDB.getRole() == CustomerRole.ADMIN) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"non puoi modificare un admin");
        mapper.fromDtoEditAdminToCustomer(customer,customerDB);

        try {
            repository.save(customerDB);
            repository.flush();
        }catch (ConstraintViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"numero di telefono già in uso");
        }
        return true;
    }


    @Override
    public Customer getCustomerFromEmailReservation(String email) {
        Customer customer = repository.findCustomerByEmail(email).orElse(null);
        if(customer == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"cliente non esistente");
        if(customer.getRole() == CustomerRole.ADMIN) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"non puoi inserire una prenotazione su di un admin");
        return  customer;
    }

    @Override
    public CustomerSafeProjection getCustomerFromID(Long id) {
        CustomerSafeProjection customer = repository.findCustomerDtoSafeByID(id).orElse(null);
        if(customer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"cliente non trovato");
        return customer;
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
