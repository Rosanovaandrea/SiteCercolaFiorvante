package com.example.SiteCercolaFioravante.integration_service_tests;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.CustomerRole;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoEditAdmin;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.MapperCustomer;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.example.SiteCercolaFioravante.customer.services.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CustomerServiceTest {
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final MapperCustomer mapperCustomer;
    private Customer customer1;

    public CustomerServiceTest(
                        @Autowired CustomerService customerService,
                        @Autowired CustomerRepository customerRepository,
                        @Autowired MapperCustomer mapperCustomer
    ){

        this.customerRepository = customerRepository;
        this.mapperCustomer = mapperCustomer;
        this.customerService = customerService;
    }
    @BeforeEach
    void init(){
        customer1 = new Customer();
        customer1.setRole(CustomerRole.CUSTOMER_IN_LOCO);
        customer1.setEmail("arosanova@gmail.com");
        customer1.setName("andrea");
        customer1.setSurname("rossi");
        customer1.setPassword("ciao");
        customer1.setPhoneNumber("1111111111");
        customerRepository.saveAndFlush(customer1);
    }

    @Test
    void editCustomerFromAdminTest(){


        CustomerDtoEditAdmin customer = new CustomerDtoEditAdmin(
                customer1.getEmail(),"capocchia","ardemus", CustomerRole.CUSTOMER_IN_LOCO,null,null
        );

        customerService.editCustomerFromAdmin(customer);
        Assertions.assertEquals(customer.surname(),customerService.getCustomerFromEmail(customer1.getEmail()).getSurname());
    }

}
