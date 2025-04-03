package com.example.SiteCercolaFioravante.integration_service_tests;

import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.MapperCustomer;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.example.SiteCercolaFioravante.customer.services.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CustomerServiceTest {
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final MapperCustomer mapperCustomer;

    public CustomerServiceTest(
                        @Autowired CustomerService customerService,
                        @Autowired CustomerRepository customerRepository,
                        @Autowired MapperCustomer mapperCustomer
    ){

        this.customerRepository = customerRepository;
        this.mapperCustomer = mapperCustomer;
        this.customerService = customerService;
    }

    @Test
    void insertCustomerFromAdminTest(){
        CustomerDtoSafe customer = new CustomerDtoSafe(
                "andrea","rosanova","arosanova@gmail.com","3333333333"
        );
        customerService.insertCustomerFromAdmin(customer);
        Assertions.assertEquals(customer.surname(),customerService.getCustomerByNameOrSurname(customer.surname()).get(0).surname());
            }

}
