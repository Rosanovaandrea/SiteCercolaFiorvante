package com.example.SiteCercolaFioravante.repository_tests;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.CustomerRole;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoListProjection;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.swing.text.html.Option;

import java.util.Optional;

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
        testCustomer.setRole(CustomerRole.CUSTOMER);
        testCustomer.setPhoneNumber("3333333333");
        customerRepository.save(testCustomer);
        customerRepository.flush();
    }



    @Test
    void givenUser_whenSaved_thenCanBeFoundByEmail() {

        CustomerDtoListProjection savedUser =customerRepository.getCustomerByNameOrSurname(testCustomer.getName()).get(0);
        assertEquals(testCustomer.getEmail(), savedUser.getEmail());
        assertEquals(testCustomer.getName(), savedUser.getName());
    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundBynameUsername() {

        CustomerDtoListProjection savedUser = customerRepository.getCustomerByNameOrSurname(testCustomer.getName()).get(0);
        assertEquals(testCustomer.getEmail(), savedUser.getEmail());
        assertEquals(testCustomer.getName(), savedUser.getName());
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
        assertEquals(customer.getEmail(),testCustomer.getEmail());
    }

    @Test
    void findCustomerByRoleTest(){
        Optional<Customer> customerOpt = customerRepository.findCustomerByRole(CustomerRole.CUSTOMER);
        Customer customer = null;
        if(customerOpt.isPresent()) customer = customerOpt.get();
        assertEquals(customer.getEmail(),testCustomer.getEmail());
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        customerRepository.delete(testCustomer);
    }
}
