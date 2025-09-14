package com.example.SiteCercolaFioravante.repository_tests;

import com.example.SiteCercolaFioravante.customer.Credentials;
import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.CustomerRole;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoListProjection;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerSafeProjection;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class CustomerRepositoryTest {

    private static Customer testCustomerInLoco;
    private static Customer testCustomer;
    private static Customer testAdmin;


    private static CustomerRepository customerRepository;

    @BeforeAll
    public static void init(@Autowired CustomerRepository repository) {

        Faker faker = new Faker();

        customerRepository = repository;
        // Initialize test data before each test method
        testCustomerInLoco = new Customer();

        testCustomerInLoco.setName("antonio");
        testCustomerInLoco.setSurname("antretta");
        testCustomerInLoco.setRole(CustomerRole.CUSTOMER_IN_LOCO);
        testCustomerInLoco.setPhoneNumber(faker.number().digits(10));



        testCustomer = new Customer();

        testCustomer.setName("carlo");
        testCustomer.setSurname("anpico");
        testCustomer.setPhoneNumber(faker.number().digits(10));
        testCustomer.setRole(CustomerRole.CUSTOMER);

        Credentials credentials = new Credentials();

        credentials.setEmail(faker.internet().emailAddress());
        credentials.setPassword(faker.internet().password());

        credentials.setCustomer(testCustomer);
        testCustomer.setCredentials(credentials);



        testAdmin = new Customer();
        testAdmin.setName("carminella");
        testAdmin.setSurname("catino");
        testAdmin.setPhoneNumber(faker.number().digits(10));
        testAdmin.setRole(CustomerRole.ADMIN);

        credentials = new Credentials();
        credentials.setEmail(faker.internet().emailAddress());
        credentials.setPassword(faker.internet().password());
        credentials.setCustomer(testAdmin);

        testAdmin.setCredentials(credentials);

        customerRepository.save(testAdmin);
        customerRepository.save(testCustomer);
        customerRepository.save(testCustomerInLoco);
        customerRepository.flush();

    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundByName() {


        List<CustomerDtoListProjection> usersFound = customerRepository.getCustomerByNameOrSurname(testCustomerInLoco.getName().substring(0,testCustomerInLoco.getName().length()/2));
        assertEquals(1, usersFound.size());
        assertTrue(
                usersFound.get(0).getName().equals(testCustomerInLoco.getName()) &&
                        usersFound.get(0).getSurname().equals(testCustomerInLoco.getSurname())
        );
    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundByPhoneNumber() {

        List<CustomerDtoListProjection> usersFound = customerRepository.getCustomerByNameOrSurname(testCustomerInLoco.getPhoneNumber());
        assertEquals(1, usersFound.size());
        assertTrue(
                usersFound.get(0).getName().equals(testCustomerInLoco.getName()) &&
                        usersFound.get(0).getSurname().equals(testCustomerInLoco.getSurname())
        );
    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundMultipleMatch() {
        String search = "ant";
        List<CustomerDtoListProjection> usersFound = customerRepository.getCustomerByNameOrSurname(search);
        assertEquals(1, usersFound.size());
        assertTrue(
                usersFound.get(0).getName().equals(testCustomerInLoco.getName()) &&
                        usersFound.get(0).getSurname().equals(testCustomerInLoco.getSurname())
        );
    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundByMistCase() {

        String mistCase = "AnToNiO";

        List<CustomerDtoListProjection> usersFound = customerRepository.getCustomerByNameOrSurname(mistCase);
        assertEquals(1, usersFound.size());
        assertTrue(
                usersFound.get(0).getName().equals(testCustomerInLoco.getName()) &&
                        usersFound.get(0).getSurname().equals(testCustomerInLoco.getSurname())
        );

    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundMultipleCustomer() {

        String mistCase = "An";

        List<CustomerDtoListProjection> usersFound = customerRepository.getCustomerByNameOrSurname(mistCase);
        assertEquals(2, usersFound.size());


    }

    @Test
    void NoResult_withAdminSearch_thenCanBeFoundByName() {


        List<CustomerDtoListProjection> userFound = customerRepository.getCustomerByNameOrSurname(testAdmin.getName());
        assertTrue(userFound.isEmpty());

    }



    //case to manage in service layer
    @Test
    void givenUser_EmptyString_AllResult() {
         String emptyString = "";

        List<CustomerDtoListProjection> userFound = customerRepository.getCustomerByNameOrSurname(emptyString);
        assertFalse(userFound.isEmpty());

    }

    @Test
    void givenUser_NoPresentDats_thenCanBeFoundByEmail() {

        String notPresentCustomer = "$%&/!(";

        List<CustomerDtoListProjection> userFound = customerRepository.getCustomerByNameOrSurname(notPresentCustomer);
        assertTrue(userFound.isEmpty());

    }

    @Test
    void geCustomerByEmail(){
        Customer customer = customerRepository.findCustomerByEmail(testCustomer.getCredentials().getEmail()).orElse(null);

        assertNotNull(customer);

        assertEquals(testCustomer.getCredentials().getEmail(),customer.getCredentials().getEmail());

    }

    @Test
    void geAdminByEmail(){
        Customer customer = customerRepository.findCustomerByEmail(testAdmin.getCredentials().getEmail()).orElse(null);

        assertNotNull(customer);

        assertEquals(testAdmin.getCredentials().getEmail(),customer.getCredentials().getEmail());

    }

    @Test
    void getNullByEmail(){
        String wrongEmail = "";
        Customer customer = customerRepository.findCustomerByEmail(wrongEmail).orElse(null);
        assertNull(customer);

    }

    @Test
    void notGetAdminByIdTest(){
        Customer customer = customerRepository.findCustomerByEmail(testAdmin.getCredentials().getEmail()).orElse(null);
        assertNotNull(customer);
        CustomerSafeProjection customerDtoSafe = customerRepository.findCustomerDtoSafeByID(customer.getId()).orElse(null);
        assertNull(customerDtoSafe);
    }

    @Test
    void getCustomerByIdTest(){
        Customer customer = customerRepository.findCustomerByEmail(testCustomer.getCredentials().getEmail()).orElse(null);
        assertNotNull(customer);
        CustomerSafeProjection customerDtoSafe = customerRepository.findCustomerDtoSafeByID(customer.getId()).orElse(null);
        assertNotNull(customerDtoSafe);
        assertEquals(customer.getId(),customerDtoSafe.getId());
    }

    @Test
    void getCustomerInLocoByIdTest(){
        CustomerDtoListProjection customer = customerRepository.getCustomerByNameOrSurname(testCustomerInLoco.getPhoneNumber()).get(0);
        assertNotNull(customer);
        CustomerSafeProjection customerDtoSafe = customerRepository.findCustomerDtoSafeByID(customer.getId()).orElse(null);
        assertNotNull(customerDtoSafe);
        assertEquals(customer.getId(),customerDtoSafe.getId());
    }

    @Test
    void customerNotFoundByIdTest(){
        Long id = 999999L;
        CustomerSafeProjection customerDtoSafe = customerRepository.findCustomerDtoSafeByID(id).orElse(null);
        assertNull(customerDtoSafe);
    }

    @Test
    void findAdminByRoleTest(){
        Customer customer = customerRepository.findCustomerByRole(CustomerRole.ADMIN).orElse(null);
        assertNotNull(customer);
        assertEquals(testAdmin.getRole(),customer.getRole());
        assertEquals(testAdmin.getPhoneNumber(),customer.getPhoneNumber());
    }

    @Test
    void findCustomerByRoleTest(){
        Customer customer = customerRepository.findCustomerByRole(CustomerRole.CUSTOMER).orElse(null);
        assertNotNull(customer);
        assertEquals(testCustomer.getRole(),customer.getRole());
        assertEquals(testCustomer.getPhoneNumber(),customer.getPhoneNumber());
    }

    @Test
    void findCustomerInLocoByRoleTest(){
        Customer customer = customerRepository.findCustomerByRole(CustomerRole.CUSTOMER_IN_LOCO).orElse(null);
        assertNotNull(customer);
        assertEquals(testCustomerInLoco.getRole(),customer.getRole());
        assertEquals(testCustomerInLoco.getPhoneNumber(),customer.getPhoneNumber());

    }



    @AfterAll
    public static void tearDown() {
        // Release test data after each test method
        customerRepository.delete(testCustomerInLoco);
        customerRepository.delete(testAdmin);
        customerRepository.delete(testCustomer);
    }
}
