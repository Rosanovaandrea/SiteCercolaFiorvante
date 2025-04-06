package com.example.SiteCercolaFioravante.integration_service_tests;

import com.example.SiteCercolaFioravante.customer.CustomerRole;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoEditAdmin;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoList;
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




    @Test
    @Order(1)
    void insertCustomerFromAdminTest(){


        CustomerDtoSafe customer = new CustomerDtoSafe(
                "andrea","rossi","","1111114111"
        );

        customerService.insertCustomerFromAdmin(customer);
        Assertions.assertEquals(customer.surname(),customerService.getCustomerByNameOrSurname(customer.name()).get(0).surname());
    }


    @Test
    @Order(2)
    void editCustomerFromAdminTest(){
        //simulate choose from customer list
        CustomerDtoList customerDtoList = customerService.getCustomerByNameOrSurname("rossi").get(0);

        //simulate Customer edit
        CustomerDtoSafe customerDtoSafe = customerService.getCustomerFromEmail(customerDtoList.email());
        CustomerDtoEditAdmin customer1 = new CustomerDtoEditAdmin(
                customerDtoSafe.email(),"bianchi","ardemus", CustomerRole.CUSTOMER_IN_LOCO,null,null
        );
        //

        customerService.editCustomerFromAdmin(customer1);
        Assertions.assertEquals(customer1.surname(),customerService.getCustomerByNameOrSurname(customer1.surname()).get(0).surname());
    }

}
