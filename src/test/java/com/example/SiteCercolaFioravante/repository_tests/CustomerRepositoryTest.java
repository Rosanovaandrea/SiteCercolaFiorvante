package com.example.SiteCercolaFioravante.repository_tests;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.CustomerRole;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoList;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
public class CustomerRepositoryTest {

    private Customer testCustomer;

    private final CustomerRepository customerRepository;

    public CustomerRepositoryTest(@Autowired CustomerRepository repository){
        customerRepository = repository;
    }

    @BeforeEach
    public void init() {
        // Initialize test data before each test method
        testCustomer = new Customer();
        testCustomer.setEmail("ardemus@gmail.com");
        testCustomer.setName("andres");
        testCustomer.setSurname("rosanova");
        testCustomer.setPassword("cheeks");
        testCustomer.setRole(CustomerRole.ADMIN);
        testCustomer.setPhoneNumber("3333333333");
        customerRepository.save(testCustomer);
        customerRepository.flush();
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

    @Test
    void findCustomerByEmailTest(){
        Customer customer = customerRepository.findCustomerByEmail(testCustomer.getEmail()).orElse(null);
        Assertions.assertEquals(customer.getEmail(),testCustomer.getEmail());
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        customerRepository.delete(testCustomer);
    }
}
