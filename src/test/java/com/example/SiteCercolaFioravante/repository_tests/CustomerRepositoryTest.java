package com.example.SiteCercolaFioravante.repository_tests;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.repository.CustomerProjectionList;
import com.example.SiteCercolaFioravante.customer.repository.CustomerProjectionSingle;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;



@DataJpaTest
public class CustomerRepositoryTest {

    private Customer testCustomer;

    @Autowired
    private CustomerRepository customerRepository;
    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method
        testCustomer = new Customer();
        testCustomer.setEmail("ardemus@gnail.com");
        testCustomer.setName("andres");
        testCustomer.setSurname("rosanova");
        testCustomer.setRole("ADMIN");
        testCustomer.setPhoneNumber(3333333333L);
        customerRepository.save(testCustomer);
        customerRepository.flush();
    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundById() {

        Customer savedCustomer =  customerRepository.findById(testCustomer.getEmail()).orElse(null);
        assertEquals(testCustomer.getEmail(), savedCustomer.getEmail());
        assertEquals(testCustomer.getName(), savedCustomer.getName());
    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundByEmail() {

        CustomerProjectionList savedUser =customerRepository.getCustomerByEmail(testCustomer.getEmail()).get(0);
        assertEquals(testCustomer.getEmail(), savedUser.getEmail());
        assertEquals(testCustomer.getName(), savedUser.getName());
    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundBynameUsername() {

        CustomerProjectionList savedUser = customerRepository.getCustomerByNameOrSurname(testCustomer.getName()).get(0);
        assertEquals(testCustomer.getEmail(), savedUser.getEmail());
        assertEquals(testCustomer.getName(), savedUser.getName());
    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundByPhoneNumber() {

        CustomerProjectionList savedUser = customerRepository.getCustomerByPhoneNumber(testCustomer.getPhoneNumber()).get(0);
        assertEquals(testCustomer.getEmail(), savedUser.getEmail());
        assertEquals(testCustomer.getName(), savedUser.getName());
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        customerRepository.delete(testCustomer);
    }
}
