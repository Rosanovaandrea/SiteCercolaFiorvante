package com.example.SiteCercolaFioravante.customer.services.impl;

import com.example.SiteCercolaFioravante.customer.*;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.*;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.example.SiteCercolaFioravante.customer.services.CustomerService;
import com.example.SiteCercolaFioravante.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final MapperCustomer mapper;

    @Override
    public List<CustomerDtoListProjection> getCustomerByNameOrSurname(String nameSurname) {
        if(nameSurname == null || nameSurname.isBlank()) return Collections.emptyList();
        return repository.getCustomerByNameOrSurname(nameSurname);
    }



    @Transactional
    @Override
    public boolean insertCustomerFromAdmin(CustomerDtoSafe customer) {



        Customer customerDB = mapper.fromDtoSafeToCustomer(customer);

        if(repository.existsByPhoneNumber(customerDB.getPhoneNumber())) {
            log.warn("si è cercato di inserire un utente con un numero già esistente");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"numero di telefono già in uso");
        }

        customerDB.setRole(CustomerRole.CUSTOMER_IN_LOCO);


            repository.save(customerDB);
            repository.flush();
            return true;



    }





    @Transactional
    @Override
    public boolean editCustomerFromAdmin(CustomerDtoEditAdmin customer) {
        Customer customerDB = repository.findById(customer.id()).orElse(null);

        if (customerDB == null) {
            log.warn("si è cercato di modificare un utente non esisstente");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"utente non esistente");
        }

        if(customerDB.getRole() == CustomerRole.ADMIN) {
            log.warn("si è cercato di modificare un utente admin");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"non puoi modificare un admin");
        }

        mapper.fromDtoEditAdminToCustomer(customer,customerDB);

        if(repository.existsByPhoneNumber(customerDB.getPhoneNumber())) {
            log.warn("si è cercato di modificare un utente con un numero già esistente");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"numero di telefono già in uso");
        }

            repository.save(customerDB);
            repository.flush();
            return true;



    }

    @Override
    public Customer getCustomerById(Long id){
        return repository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteCustomer(Long id) {

        Customer customerToDelete = repository.findById(id).orElse(null);



            if(customerToDelete == null) {
                log.warn("si è tentato di cancellare un utente non esistente");
                throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"operazione non valida");
            }

            if(customerToDelete.getRole().equals(CustomerRole.ADMIN)){
                log.warn("si è tentato di cancellare un utente admim");
                throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"operazione non valida");
                }


            repository.deleteById(id);

        return true;
    }

    @Override
    public CustomerSafeProjection getCustomerFromID(Long id) {
        CustomerSafeProjection customer = repository.findCustomerDtoSafeByID(id).orElse(null);
        if (customer == null) {
            log.warn("si è cercato di ottenere il cliente con un id non esistente");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"cliente non trovato");
        }
        return customer;
    }


}
