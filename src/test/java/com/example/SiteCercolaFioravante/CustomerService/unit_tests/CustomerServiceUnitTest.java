package com.example.SiteCercolaFioravante.CustomerService.unit_tests;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.CustomerRole;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoListProjection;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.MapperCustomer;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.example.SiteCercolaFioravante.customer.services.impl.CustomerServiceImpl;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CustomerServiceUnitTest {

    @Mock
    private CustomerRepository repository;

    @Mock
    private MapperCustomer mapperCustomer;

    @InjectMocks
    private CustomerServiceImpl service;

    private Faker faker;

    @BeforeEach
    public void init(){
       this.faker = new Faker();
    }


    @Test
    void CustomerServiceSearchTest(){
        CustomerDtoListProjection element = Mockito.mock();
        String search = "search";
        List<CustomerDtoListProjection> list = new LinkedList<>();
        list.add(element);
        Mockito.when(repository.getCustomerByNameOrSurname(Mockito.anyString())).thenReturn(list);
        List<CustomerDtoListProjection> result = service.getCustomerByNameOrSurname(search);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(element,result.get(0));
    }

    @Test
    void CustomerServiceBlankSearch(){
        String search = " ";
        List<CustomerDtoListProjection> result = service.getCustomerByNameOrSurname(search);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void CustomerServiceZeroSizeSearch(){
        String search = "";
        List<CustomerDtoListProjection> result = service.getCustomerByNameOrSurname(search);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void CustomerServiceNullSearch(){
        String search = null;
        List<CustomerDtoListProjection> result = service.getCustomerByNameOrSurname(search);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void insertCustomerFromAdminRight(){
        Customer customer = Mockito.spy(new Customer());
        CustomerDtoSafe data = Mockito.mock();
        Mockito.when(mapperCustomer.fromDtoSafeToCustomer(data)).thenReturn(customer);
        customer.setPhoneNumber(faker.number().digits(10));

        Mockito.when(repository.existsByPhoneNumber(customer.getPhoneNumber())).thenReturn(false);

        Mockito.when(repository.save(customer)).thenReturn(customer);
        Mockito.doNothing().when(repository).flush();

        boolean result = service.insertCustomerFromAdmin(data);
        Assertions.assertTrue(result);

        Mockito.verify(repository,Mockito.times(1)).existsByPhoneNumber(customer.getPhoneNumber());
        Mockito.verify(repository,Mockito.times(1)).save(customer);
        Mockito.verify(repository,Mockito.times(1)).flush();
        Mockito.verify(customer,Mockito.times(1)).setRole(CustomerRole.CUSTOMER_IN_LOCO);

    }

    @Test
    void insertCustomerFromAdminErrorDuplicatePhoneNumber(){
        Customer customer = Mockito.spy(new Customer());
        CustomerDtoSafe data = Mockito.mock();
        Mockito.when(mapperCustomer.fromDtoSafeToCustomer(data)).thenReturn(customer);
        customer.setPhoneNumber(faker.number().digits(10));

        Mockito.when(repository.existsByPhoneNumber(customer.getPhoneNumber())).thenReturn(true);


        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class,()->service.insertCustomerFromAdmin(data));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST,e.getStatusCode());


        Mockito.verify(repository,Mockito.times(1)).existsByPhoneNumber(customer.getPhoneNumber());
        Mockito.verify(repository,Mockito.times(0)).save(customer);
        Mockito.verify(repository,Mockito.times(0)).flush();
        Mockito.verify(customer,Mockito.times(0)).setRole(CustomerRole.CUSTOMER_IN_LOCO);

    }

    @Test
    void insertCustomerFromAdminGenericError(){
        CustomerDtoSafe data = Mockito.mock();
        Mockito.when(mapperCustomer.fromDtoSafeToCustomer(data)).thenThrow(RuntimeException.class);

        Assertions.assertThrows(Exception.class,()->service.insertCustomerFromAdmin(data));
    }
}
