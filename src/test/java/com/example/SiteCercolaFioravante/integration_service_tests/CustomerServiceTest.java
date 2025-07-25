package com.example.SiteCercolaFioravante.integration_service_tests;

import com.example.SiteCercolaFioravante.customer.CustomerRole;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoEditAdmin;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoListProjection;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.MapperCustomer;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.example.SiteCercolaFioravante.customer.services.CustomerService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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


}
