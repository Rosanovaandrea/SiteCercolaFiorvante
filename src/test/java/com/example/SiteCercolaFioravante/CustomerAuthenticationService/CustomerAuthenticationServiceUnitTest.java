package com.example.SiteCercolaFioravante.CustomerAuthenticationService;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.MapperCustomer;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.example.SiteCercolaFioravante.customer.services.impl.AuthenticationStaticLibraryWrapper;
import com.example.SiteCercolaFioravante.customer.services.impl.CustomerAuthenticationServiceImpl;
import com.example.SiteCercolaFioravante.utils.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CustomerAuthenticationServiceUnitTest {

    private AuthenticationStaticLibraryWrapper wrapper;
    private JwtUtils jwtUtils;
    private CustomerRepository repository;
    private MapperCustomer mapper;
    private JavaMailSender emailSender;
    private CustomerAuthenticationServiceImpl service;

    @BeforeEach
    void setUp(){
        wrapper= Mockito.mock(AuthenticationStaticLibraryWrapper.class);
        jwtUtils = Mockito.mock(JwtUtils.class);
        repository = Mockito.mock(CustomerRepository.class);
        mapper = Mockito.mock(MapperCustomer.class);
        emailSender = Mockito.mock(JavaMailSender.class);
        service = Mockito.spy(new CustomerAuthenticationServiceImpl(wrapper,jwtUtils,repository,mapper,emailSender));
    }

    @Test
    void doEmailPasswordResetRightTest(){

        String emaIL ="example@example.it";

        Customer customer = Mockito.spy(new Customer());
        Mockito.doNothing().when(customer).setTokenRegistration(Mockito.any());

        Mockito.when(customer.getId()).thenReturn(1L);
        UUID mockUuid = UUID.fromString("68307970-bb6a-44df-8566-8c51de0089ce");

        Mockito.when(wrapper.getUUID()).thenReturn(mockUuid);
        Mockito.when(repository.findCustomerByEmail(emaIL)).thenReturn(Optional.of(customer));
        Mockito.when(jwtUtils.createResetPasswordToken(Mockito.anyString(),Mockito.anyString())).thenReturn("token");
        Mockito.when(repository.saveAndFlush(Mockito.any())).thenReturn(null);
        Mockito.doNothing().when(service).sendMessageEmail(Mockito.anyString(),Mockito.anyString(),Mockito.anyString());

        service.doEmailPasswordReset(emaIL);

        Mockito.verify(repository,Mockito.times(1)).findCustomerByEmail(emaIL);
        Mockito.verify(wrapper,Mockito.times(1)).getUUID();
        Mockito.verify(jwtUtils,Mockito.times(1)).createResetPasswordToken(Mockito.any(),Mockito.any());
        Mockito.verify(repository,Mockito.times(1)).saveAndFlush(customer);
        Mockito.verify(service,Mockito.times(1)).sendMessageEmail(Mockito.eq(emaIL),Mockito.anyString(),Mockito.anyString());

    }

    @Test
    void doEmailPasswordResetCustomerNotFoundTest(){

        String emaIL ="example@example.it";

        Mockito.when(repository.findCustomerByEmail(emaIL)).thenReturn(Optional.empty());

        service.doEmailPasswordReset(emaIL);

        Mockito.verify(repository,Mockito.times(1)).findCustomerByEmail(emaIL);
        Mockito.verify(wrapper,Mockito.times(0)).getUUID();
        Mockito.verify(jwtUtils,Mockito.times(0)).createResetPasswordToken(Mockito.any(),Mockito.any());
        Mockito.verify(repository,Mockito.times(0)).saveAndFlush(Mockito.any());
        Mockito.verify(service,Mockito.times(0)).sendMessageEmail(Mockito.eq(emaIL),Mockito.anyString(),Mockito.anyString());

    }

    @Test
    void doEmailPasswordResetExceptionTest(){

        String emaIL ="example@example.it";

        Mockito.when(repository.findCustomerByEmail(emaIL)).thenThrow(RuntimeException.class);

        Assertions.assertThrows(RuntimeException.class,()->{service.doEmailPasswordReset(emaIL);});

    }
}
