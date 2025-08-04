package com.example.SiteCercolaFioravante.integration_service_tests;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.CustomerRole;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.*;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.example.SiteCercolaFioravante.customer.services.CustomerService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerServiceTest {
    private static CustomerService customerService;
    private static CustomerRepository customerRepository;
    private static Customer customer;
    private static Customer customer1;
    private static Customer customer2;
    private static Customer customer3;

    @BeforeAll
    static void initialization(@Autowired CustomerService customerServiceSet, @Autowired CustomerRepository customerRepositorySet){

        customerService = customerServiceSet;
        customerRepository = customerRepositorySet;

        customer = new Customer();
        customer.setName("carla");
        customer.setSurname("esposito");
        customer.setEmail("carlaesposito@email.com");
        customer.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
        customer.setPhoneNumber("3462842080");
        customer.setRole(CustomerRole.ADMIN);

        customer1 = new Customer();
        customer1.setName("antonio");
        customer1.setSurname("casilli");
        customer1.setPhoneNumber("3462847980");
        customer1.setRole(CustomerRole.CUSTOMER_IN_LOCO);

        customer2 = new Customer();
        customer2.setName("andrea");
        customer2.setSurname("casarano");
        customer2.setPhoneNumber("3462833798");
        customer2.setRole(CustomerRole.CUSTOMER_IN_LOCO);

         customer3 = new Customer();
        customer3.setName("João"); // Carattere speciale
        customer3.setSurname("Sørensen"); // Carattere speciale
        customer3.setPhoneNumber("3457654321");
        customer3.setRole(CustomerRole.CUSTOMER_IN_LOCO);


        customerRepository.saveAndFlush(customer);
        customerRepository.saveAndFlush(customer1);
        customerRepository.saveAndFlush(customer2);
        customerRepository.saveAndFlush(customer3);

    }

    @Test
    @Order(1)
    void testSearchCustomer(){

       List<CustomerDtoListProjection> customerSearch = customerService.getCustomerByNameOrSurname(customer1.getName());
        Assertions.assertEquals(1, customerSearch.size());
        Assertions.assertEquals(customerSearch.get(0).getId(),customer1.getId());

        customerSearch = customerService.getCustomerByNameOrSurname(customer1.getSurname());
        Assertions.assertEquals(1, customerSearch.size());
        Assertions.assertEquals(customerSearch.get(0).getId(),customer1.getId());

        customerSearch = customerService.getCustomerByNameOrSurname(customer1.getPhoneNumber());
        Assertions.assertEquals(1, customerSearch.size());
        Assertions.assertEquals(customerSearch.get(0).getId(),customer1.getId());

        //test for special characters
        // implementato via postman per ragioni di tempo e soldi
        //

        // test for deduplication
        customerSearch = customerService.getCustomerByNameOrSurname("an");
        Assertions.assertEquals(2, customerSearch.size());

        customerSearch = customerService.getCustomerByNameOrSurname("ca");
        Assertions.assertEquals(2, customerSearch.size());

        //test case insensitive
        customerSearch = customerService.getCustomerByNameOrSurname("CA");
        Assertions.assertEquals(2, customerSearch.size());

        // test exclusion admin
        customerSearch = customerService.getCustomerByNameOrSurname("346");
        Assertions.assertEquals(2, customerSearch.size());
    }


    @Test
    void testSearchCustomerWrong(){

        //control that the service doesn't return admins
        List<CustomerDtoListProjection> customerSearch = customerService.getCustomerByNameOrSurname(customer.getName());
        Assertions.assertEquals(0, customerSearch.size());

        customerSearch = customerService.getCustomerByNameOrSurname(customer.getSurname());
        Assertions.assertEquals(0, customerSearch.size());

        customerSearch = customerService.getCustomerByNameOrSurname(customer.getPhoneNumber());
        Assertions.assertEquals(0, customerSearch.size());
        //

        //control that the query doesn't return anything on nonexistent data
        customerSearch = customerService.getCustomerByNameOrSurname("bu");
        Assertions.assertEquals(0, customerSearch.size());

        customerSearch = customerService.getCustomerByNameOrSurname("4");
        Assertions.assertEquals(0, customerSearch.size());
        //

        //edge case for blank string
        customerSearch = customerService.getCustomerByNameOrSurname("");
        Assertions.assertEquals(0, customerSearch.size());

        customerSearch = customerService.getCustomerByNameOrSurname("             ");
        Assertions.assertEquals(0, customerSearch.size());
        //

    }

    @Test
    @Order(2)
    void testEditFromAdmin(){
        String newName="giampierazzo";
        CustomerDtoEditAdmin customerDtoEditAdmin = new CustomerDtoEditAdmin(
          customer1.getId(),customer1.getSurname(),newName,customer1.getRole(),customer1.getPhoneNumber()
        );
        customerService.editCustomerFromAdmin(customerDtoEditAdmin);
        Optional<Customer> result = customerRepository.findById(customer1.getId());
        Assertions.assertTrue(result.isPresent());
        Customer customerEdit = result.get();
        Assertions.assertEquals(customer1.getId(),customerEdit.getId());
        Assertions.assertEquals(newName,customerEdit.getName());
        customer1= customerEdit; // important to synchronize the customer with database
    }


    @Test
    void testEditFromAdminWrong(){
       final CustomerDtoEditAdmin customerDtoEditAdminNoAdmins = new CustomerDtoEditAdmin(
                customer.getId(),customer.getSurname(),customer.getName(),customer.getRole(),customer.getPhoneNumber()
        );

       ResponseStatusException e= Assertions.assertThrows(ResponseStatusException.class,()->{customerService.editCustomerFromAdmin(customerDtoEditAdminNoAdmins);});

       Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());


       final CustomerDtoEditAdmin customerDtoEditAdminWrongId = new CustomerDtoEditAdmin(
                1000L,customer.getSurname(),customer.getName(),customer.getRole(),customer.getPhoneNumber()
        );

       e= Assertions.assertThrows(ResponseStatusException.class,()->{customerService.editCustomerFromAdmin(customerDtoEditAdminWrongId);});

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());

    }

    @Test
    void testSearchFromID(){
        Assertions.assertEquals(customer1.getId(),customerService.getCustomerFromID(customer1.getId()).getId());
    }

    @Test
    void testSearchFromIDWrong(){
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class,()->{customerService.getCustomerFromID(1000L);});
        Assertions.assertEquals(HttpStatus.NOT_FOUND,e.getStatusCode());
    }

    @Test
    void testInsertCustomer(){
        CustomerDtoSafe insert = new CustomerDtoSafe("castello","antonio","3233233232");
        customerService.insertCustomerFromAdmin(insert);
    }

    @Test
    void testInsertCustomerNumberAlredyExistent(){
       final CustomerDtoSafe insert = new CustomerDtoSafe("castello","antonio",customer.getPhoneNumber());
       ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class,()->{customerService.insertCustomerFromAdmin(insert);});
       Assertions.assertEquals(HttpStatus.BAD_REQUEST,e.getStatusCode());
    }




    @AfterAll
    static void cleanup(){
        customerRepository.deleteAll();
        customerRepository.flush();
    }


}
