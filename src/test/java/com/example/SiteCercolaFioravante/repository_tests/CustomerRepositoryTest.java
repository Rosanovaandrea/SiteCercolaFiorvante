package com.example.SiteCercolaFioravante.repository_tests;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.CustomerDtoList;
import com.example.SiteCercolaFioravante.customer.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

        CustomerDtoList savedUser =customerRepository.getCustomerByEmail(testCustomer.getEmail()).get(0);
        assertEquals(testCustomer.getEmail(), savedUser.email());
        assertEquals(testCustomer.getName(), savedUser.name());
    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundBynameUsername() {

        CustomerDtoList savedUser = customerRepository.getCustomerByNameOrSurname(testCustomer.getName()).get(0);
        assertEquals(testCustomer.getEmail(), savedUser.email());
        assertEquals(testCustomer.getName(), savedUser.name());
    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundByPhoneNumber() {

        CustomerDtoSafe savedUser = customerRepository.getCustomerByPhoneNumber(testCustomer.getPhoneNumber());
        assertEquals(testCustomer.getEmail(), savedUser.email());
        assertEquals(testCustomer.getName(), savedUser.name());
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        customerRepository.delete(testCustomer);
    }
}
