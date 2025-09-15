package com.example.SiteCercolaFioravante.CustomerAuthenticationService;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.SiteCercolaFioravante.customer.Credentials;
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
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CustomerAuthenticationServiceUnitTest {

    private AuthenticationStaticLibraryWrapper wrapper;
    private JwtUtils jwtUtils;
    private CustomerRepository repository;
    private JavaMailSender emailSender;
    private CustomerAuthenticationServiceImpl service;

    @BeforeEach
    void setUp(){
        wrapper= Mockito.mock(AuthenticationStaticLibraryWrapper.class);
        jwtUtils = Mockito.mock(JwtUtils.class);
        repository = Mockito.mock(CustomerRepository.class);
        emailSender = Mockito.mock(JavaMailSender.class);
        service = Mockito.spy(new CustomerAuthenticationServiceImpl(wrapper,jwtUtils,repository,emailSender));
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

    @Test
    void deResetPasswordRightTest(){
        String token ="token_placeholder";
        String password = "Password_placeholder";
        UUID mockUuid = UUID.fromString("68307970-bb6a-44df-8566-8c51de0089ce");
        String[] info  = {Long.toString(1L),mockUuid.toString()};

        Customer customer = new Customer();
        customer.setTokenRegistration(mockUuid.toString());
        Credentials credentials = new Credentials();
        customer.setCredentials(credentials);

        Mockito.when(jwtUtils.passwordResetJwtVerification(token)).thenReturn(info);
        Mockito.when(wrapper.setPassword(password)).thenReturn(password);
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(customer));
        Mockito.when(repository.saveAndFlush(Mockito.any())).thenReturn(null);
        service.doPasswordReset(token,password);

        Assertions.assertEquals(password,credentials.getPassword());
        Assertions.assertNull(customer.getTokenRegistration());

        Mockito.verify(jwtUtils,Mockito.times(1)).passwordResetJwtVerification(Mockito.eq(token));
        Mockito.verify(wrapper,Mockito.times(1)).setPassword(Mockito.eq(password));
        Mockito.verify(repository,Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(repository,Mockito.times(1)).saveAndFlush(Mockito.any(Customer.class));

    }

    @Test
    void deResetPasswordJWTExceptionTest(){
        String token ="token_placeholder";
        String password = "Password_placeholder";

        Mockito.when(jwtUtils.passwordResetJwtVerification(token)).thenThrow(JWTVerificationException.class);
// Set the role
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class,()->{service.doPasswordReset(token,password);});

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED,e.getStatusCode());

        Mockito.verify(jwtUtils,Mockito.times(1)).passwordResetJwtVerification(Mockito.eq(token));
        Mockito.verify(wrapper,Mockito.times(0)).setPassword(Mockito.eq(password));
        Mockito.verify(repository,Mockito.times(0)).findById(Mockito.anyLong());
        Mockito.verify(repository,Mockito.times(0)).saveAndFlush(Mockito.any(Customer.class));

    }

    @Test
    void deResetPasswordCustomerNotFoundTest(){
        String token ="token_placeholder";
        String password = "Password_placeholder";
        UUID mockUuid = UUID.fromString("68307970-bb6a-44df-8566-8c51de0089ce");
        String[] info  = {Long.toString(1L),mockUuid.toString()};

        Mockito.when(jwtUtils.passwordResetJwtVerification(token)).thenReturn(info);
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class,()->{service.doPasswordReset(token,password);});

        Assertions.assertEquals(HttpStatus.FORBIDDEN,e.getStatusCode());
// Set the role

        Mockito.verify(jwtUtils,Mockito.times(1)).passwordResetJwtVerification(Mockito.eq(token));
        Mockito.verify(wrapper,Mockito.times(0)).setPassword(Mockito.eq(password));
        Mockito.verify(repository,Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(repository,Mockito.times(0)).saveAndFlush(Mockito.any(Customer.class));

    }

    @Test
    void deResetPasswordWrongTokenTest(){
        String token ="token_placeholder";
        String password = "Password_placeholder";
        UUID mockUuid = UUID.fromString("68307970-bb6a-44df-8566-8c51de0089ce");
        UUID wrongUuid = UUID.fromString("c115bb2c-b752-4160-934e-d552f77c172c");
        String[] info  = {Long.toString(1L),wrongUuid.toString()};

        Customer customer = new Customer();
        Credentials credentials = new Credentials();
        customer.setCredentials(credentials);
        customer.setTokenRegistration(mockUuid.toString());
        Mockito.when(jwtUtils.passwordResetJwtVerification(token)).thenReturn(info);
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(customer));

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class,()->{service.doPasswordReset(token,password);});

        Assertions.assertEquals(HttpStatus.FORBIDDEN,e.getStatusCode());

        Assertions.assertNull(credentials.getPassword());
        Assertions.assertNotNull(customer.getTokenRegistration());

        Mockito.verify(jwtUtils,Mockito.times(1)).passwordResetJwtVerification(Mockito.eq(token));
        Mockito.verify(wrapper,Mockito.times(0)).setPassword(Mockito.eq(password));
        Mockito.verify(repository,Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(repository,Mockito.times(0)).saveAndFlush(Mockito.any(Customer.class));

    }

    @Test
    void doLoginRightTest(){
        String email ="test@example.com";
        String password ="password123";

        Customer customer = Mockito.spy(new Customer());
        Mockito.when(repository.findCustomerByEmail(email)).thenReturn(Optional.of(customer));
        customer.setRole(com.example.SiteCercolaFioravante.customer.CustomerRole.CUSTOMER);
        Credentials credentials = new Credentials();
        credentials.setPassword(password);
        customer.setCredentials(credentials);

        UUID mockUuid = UUID.fromString("68307970-bb6a-44df-8566-8c51de0089ce");

        Mockito.when(wrapper.getUUID()).thenReturn(mockUuid);
        Mockito.when(wrapper.checkPassword(credentials.getPassword(),password)).thenReturn(true);
        Mockito.when(jwtUtils.createRefreshToken(Mockito.anyString(),Mockito.anyString())).thenReturn("refresh_token");
        Mockito.when(jwtUtils.createAccessToken(Mockito.anyString(),Mockito.anyString())).thenReturn("access_token");

        String[] tokens = service.doLogin(email, password);

        Assertions.assertEquals("refresh_token", tokens[0]);
        Assertions.assertEquals("access_token", tokens[1]);

        Mockito.verify(repository).findCustomerByEmail(email);
        Mockito.verify(wrapper, Mockito.times(1)).getUUID();
        Mockito.verify(jwtUtils, Mockito.times(1)).createRefreshToken(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(jwtUtils, Mockito.times(1)).createAccessToken(Mockito.anyString(),Mockito.anyString());

    }

    @Test
    void doLoginCustomerNotFoundTest(){
        String email ="test@example.com";
        String password ="password123";

        Mockito.when(repository.findCustomerByEmail(email)).thenReturn(Optional.empty());

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class,()->{service.doLogin(email, password);});

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED,e.getStatusCode());
        Mockito.verify(repository).findCustomerByEmail(email);
        Mockito.verify(wrapper, Mockito.times(0)).getUUID();
        Mockito.verify(jwtUtils, Mockito.times(0)).createRefreshToken(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(jwtUtils, Mockito.times(0)).createAccessToken(Mockito.anyString(),Mockito.anyString());

    }


    @Test
    void doLoginInvalidPasswordTest(){
        String email ="test@example.com";
        String password ="wrongpassword";
        String rightPassword ="right_password";

        Customer customer = Mockito.spy(new Customer());
        Mockito.when(repository.findCustomerByEmail(email)).thenReturn(Optional.of(customer));
        customer.setRole(com.example.SiteCercolaFioravante.customer.CustomerRole.CUSTOMER);
        Credentials credentials = new Credentials();
        credentials.setPassword(rightPassword);
        customer.setCredentials(credentials);


        Mockito.when(wrapper.checkPassword(password, credentials.getPassword())).thenReturn(false);
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class,()->{service.doLogin(email, password);});

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED,e.getStatusCode());
        Mockito.verify(repository).findCustomerByEmail(email);
        Mockito.verify(wrapper).checkPassword(password, credentials.getPassword());
        Mockito.verify(jwtUtils, Mockito.never()).createRefreshToken(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(jwtUtils, Mockito.never()).createAccessToken(Mockito.anyString(),Mockito.anyString());

    }

    @Test
    void doLoginWithException(){
        String email ="test@example.com";
        String password ="password123";

        Mockito.when(repository.findCustomerByEmail(email)).thenThrow(new RuntimeException("Database error"));

        Assertions.assertThrows(RuntimeException.class,()->{service.doLogin(email, password);});

        Mockito.verify(repository).findCustomerByEmail(email);
    }

    @Test
    void testDoAuthentication() {

        String[] expectedInfo = {"user1", "ROLE_USER"};
        Mockito.when(jwtUtils.getTokenAccessId("testToken")).thenReturn(expectedInfo);

        Authentication auth = service.doAuthentication("testToken");


        Assertions.assertEquals("user1", auth.getName());


        Assertions.assertEquals("testToken", auth.getCredentials());


        Assertions.assertTrue(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void testDoAuthenticationThrowsException() {
        Mockito.when(jwtUtils.getTokenAccessId(Mockito.anyString())).thenThrow(JWTVerificationException.class);
        Assertions.assertThrows(JWTVerificationException.class, () -> service.doAuthentication("testToken"));
    }

    @Test
    void doRefreshAccessToken_ValidTokenAndCustomerExists() {

        // Scenario: Token valido, customer esistente
        Long customerId = 1L;
        String refreshToken = "validRefreshToken";
        String tokenId = "token_id";
        String role = "CUSTOMER";

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setTokenRegistration(tokenId);
        customer.setRole(com.example.SiteCercolaFioravante.customer.CustomerRole.CUSTOMER);

        Mockito.when(jwtUtils.refreshTokenVerification(refreshToken)).thenReturn(new String[]{Long.toString(customerId),tokenId});

        Mockito.when(repository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(jwtUtils.createAccessToken(customerId.toString(), role)).thenReturn("newAccessToken");

        String accessToken = service.doRefreshAccessToken(refreshToken);

        Assertions.assertEquals("newAccessToken", accessToken);
        Mockito.verify(repository).findById(customerId);
        Mockito.verify(jwtUtils).createAccessToken(customerId.toString(), role);
    }

    @Test
    void doRefreshAccessToken_InvalidTokenVerificationException() {
        // Scenario: Token non valido (JWTVerificationException)
        String invalidToken = "invalidRefreshToken";

        Mockito.when(jwtUtils.refreshTokenVerification(invalidToken)).thenThrow(new JWTVerificationException("token is invalid"));

        Assertions.assertThrows(ResponseStatusException.class, () -> service.doRefreshAccessToken(invalidToken)); // Verifica che venga lanciata l'eccezione corretta
        Mockito.verify(repository, Mockito.never()).findById(Mockito.anyLong());
        Mockito.verify(jwtUtils).refreshTokenVerification(invalidToken);
    }

    @Test
    void doRefreshAccessToken_CustomerNotFound() {
        // Scenario: Customer non trovato nel database
        String refreshToken = "validRefreshToken";
        String validTokenId = "valid_token_id";
        Long customerId = 123L;

        Mockito.when(jwtUtils.refreshTokenVerification(refreshToken)).thenReturn(new String[]{Long.toString(customerId),validTokenId});

        Mockito.when(repository.findById(customerId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class, () -> service.doRefreshAccessToken(refreshToken)); // Verifica che venga lanciata l'eccezione corretta
        Mockito.verify(repository).findById(customerId);
        Mockito.verify(jwtUtils, Mockito.never()).createAccessToken(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void doRefreshAccessToken_TokenRegistrationMismatch() {
        // Scenario: Token di registrazione non corrisponde
        Long customerId = 1L;
        String refreshToken = "validRefreshToken";
        String validTokenId = "valid_token_id";
        String oldTokenId = "old_token_id";
        String role = "CUSTOMER";

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setTokenRegistration(validTokenId);
        customer.setRole(com.example.SiteCercolaFioravante.customer.CustomerRole.CUSTOMER);


        Mockito.when(repository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(jwtUtils.refreshTokenVerification(refreshToken)).thenReturn(new String[]{Long.toString(customerId),oldTokenId});

        Assertions.assertThrows(ResponseStatusException.class, () -> service.doRefreshAccessToken(refreshToken)); // Verifica che venga lanciata l'eccezione corretta
    }

    @Test
    void doLogout_happyPath() throws Exception {
        Long customerId =1L;
        String refreshTokenID ="refresh_token_id";
        String refreshToken = "refresh_token";

        // Arrange
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setTokenRegistration(refreshTokenID);
        Mockito.when(repository.findById(customerId)).thenReturn(java.util.Optional.of(customer));
        Mockito.when(jwtUtils.refreshTokenVerification(Mockito.anyString())).thenReturn(new String[]{Long.toString(customerId), refreshTokenID});

        // Act
        boolean result = service.doLogout(refreshToken);

        // Assert
        Assertions.assertTrue(result);
        Mockito.verify(repository, Mockito.times(1)).save(customer);
        Mockito.verify(jwtUtils, Mockito.times(1)).refreshTokenVerification(Mockito.anyString());
    }

    @Test
    void doLogout_refreshTokenInvalid() throws Exception {
        // Arrange
        Long customerId =1L;
        String refreshToken ="refresh_token";
        Mockito.when(jwtUtils.refreshTokenVerification(Mockito.anyString())).thenThrow(new JWTVerificationException("Invalid token"));

        // Act & Assert
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> service.doLogout(refreshToken));
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED,e.getStatusCode());
    }

    @Test
    void doLogout_customerNotFound() throws Exception {
        Long customerId =1L;
        String refreshToken ="refresh_token";
        String refreshTokenID = "refresh_token_id";
        Mockito.when(repository.findById(customerId)).thenReturn(Optional.empty());
        Mockito.when(jwtUtils.refreshTokenVerification(Mockito.anyString())).thenReturn(new String[]{Long.toString(customerId), refreshTokenID});

        // Act & Assert
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> service.doLogout(refreshToken));
        Assertions.assertEquals(HttpStatus.FORBIDDEN,e.getStatusCode());
    }

    @Test
    void doLogout_tokenMismatch() throws Exception {
        Long customerId = 1L;
        String refreshToken = "refresh_token";
        String refreshTokenId = "refresh_token_id";
        String wrongTokenId = "wrong_token_id";
        // Arrange
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setTokenRegistration(refreshTokenId);

        Mockito.when(repository.findById(customerId)).thenReturn(java.util.Optional.of(customer));
        Mockito.when(jwtUtils.refreshTokenVerification(Mockito.anyString())).thenReturn(new String[]{Long.toString(customerId), wrongTokenId});


        // Act & Assert
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> service.doLogout(refreshToken));

        Assertions.assertEquals(HttpStatus.FORBIDDEN,e.getStatusCode());
    }


}
