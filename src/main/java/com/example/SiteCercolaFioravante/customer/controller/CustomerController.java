package com.example.SiteCercolaFioravante.customer.controller;

import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoEditAdmin;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoList;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;



    @GetMapping("/search")
    public ResponseEntity<List<CustomerDtoList>> getCustomers(@RequestParam String type, @RequestParam String query) {
        List<CustomerDtoList> customer = null;

        if(type.toLowerCase().contains("name") || type.toLowerCase().contains("surname"))
               customer = customerService.getCustomerByNameOrSurname(query);

        if(type.toLowerCase().contains("email"))
            customer = customerService.getCustomerByEmail(query);

        return new ResponseEntity<>(customer, HttpStatus.OK);

    }

    @PostMapping(value = {"/new"})
    public ResponseEntity<Boolean> createCustomer(@Valid @RequestBody CustomerDtoSafe customer) {

        boolean response = customerService.insertCustomerFromAdmin(customer);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(value = {"/update"})

    public ResponseEntity<Boolean> updateCustomer( @Valid @RequestBody CustomerDtoEditAdmin updatedBook) {

        boolean response = customerService.editCustomerFromAdmin(updatedBook);

            return new ResponseEntity<>(response,HttpStatus.OK);

    }


}