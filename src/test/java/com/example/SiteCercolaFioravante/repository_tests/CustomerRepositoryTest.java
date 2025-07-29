package com.example.SiteCercolaFioravante.repository_tests;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.CustomerRole;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoListProjection;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerSafeProjection;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.swing.text.html.Option;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class CustomerRepositoryTest {

    private static Customer testCustomer;

    private static CustomerRepository customerRepository;

    @BeforeAll
    public static void init(@Autowired CustomerRepository repository) {

        customerRepository = repository;
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
        assertEquals(testCustomer.getId(), savedUser.getId());
        assertEquals(testCustomer.getName(), savedUser.getName());

        List<CustomerDtoListProjection> userNotFound = customerRepository.getCustomerByNameOrSurname("Rossi");
        assertEquals(0, userNotFound.size());

    }



    @Test
    void findCustomerByEmailTest(){

        Customer customer = customerRepository.findCustomerByEmail(testCustomer.getEmail()).orElse(null);
        assertEquals(customer.getId(),testCustomer.getId());

        customer = customerRepository.findCustomerByEmail("67hfe@gmail.com").orElse(null);
        assertNull(customer);
    }

    @Test
    void findCustomerByRoleTest(){
        Optional<Customer> customerOpt = customerRepository.findCustomerByRole(CustomerRole.CUSTOMER);
        assertTrue(customerOpt.isPresent());

        Optional<Customer> customerOptNotPresent = customerRepository.findCustomerByRole(CustomerRole.ADMIN);
        assertTrue(customerOptNotPresent.isEmpty());
    }

     @Test
     void finCustomerById(){

        Optional<CustomerSafeProjection> customer = customerRepository.findCustomerDtoSafeByID(testCustomer.getId());
        assertTrue(customer.isPresent());
        assertEquals(testCustomer.getId(),customer.get().getId());
     }



    @AfterAll
    public static void tearDown() {
        // Release test data after each test method
        customerRepository.delete(testCustomer);
    }
}
