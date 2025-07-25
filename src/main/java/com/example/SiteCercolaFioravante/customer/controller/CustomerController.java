package com.example.SiteCercolaFioravante.customer.controller;

import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoEditAdmin;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoListProjection;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerSafeProjection;
import com.example.SiteCercolaFioravante.customer.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;



    @GetMapping("/search")
    public ResponseEntity<List<CustomerDtoListProjection>> getCustomers( @RequestParam String query) {
        List<CustomerDtoListProjection> customers = null;

        if(query != null && !query.isEmpty())
        {customers = customerService.getCustomerByNameOrSurname(query);}

        return new ResponseEntity<>(customers, HttpStatus.OK);

    }

    @GetMapping("/singleCustomer")
    public ResponseEntity<CustomerSafeProjection> getSingleCustomer(@RequestParam Long id){
        CustomerSafeProjection customer =customerService.getCustomerFromID(id);

        return new ResponseEntity<>(customer,HttpStatus.OK);
    }

    @PostMapping(value = {"/new"})
    public ResponseEntity<Boolean> createCustomer(@Valid @RequestBody CustomerDtoSafe customer) {

        boolean response = customerService.insertCustomerFromAdmin(customer);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = {"/update"})
    public ResponseEntity<Boolean> updateCustomer( @Valid @RequestBody CustomerDtoEditAdmin updatedCustomer) {

        boolean response = customerService.editCustomerFromAdmin(updatedCustomer);

            return new ResponseEntity<>(response,HttpStatus.OK);

    }


}