package com.example.SiteCercolaFioravante.integration_service_tests;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.SiteCercolaFioravante.TokenType;
import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.CustomerRole;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoComplete;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoListProjection;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.example.SiteCercolaFioravante.customer.services.CustomerAuthenticationService;
import com.example.SiteCercolaFioravante.customer.services.CustomerService;
import com.example.SiteCercolaFioravante.utils.JwtUtils;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerAuthenticationTest {
    private final CustomerAuthenticationService authenticationService;
    private final JwtUtils jwtUtils;
    private static CustomerRepository repository;
    private static final String EMAIL = "ardemus@gmail.com";
    private  static final String PASSWORD ="dedos12345";
    private static Customer customer;
    private static String refreshToken;
    private static String accessToken;
    private static GreenMail greenMail;

    public  CustomerAuthenticationTest(
            @Autowired CustomerAuthenticationService authenticationService,
            @Autowired JwtUtils jwtUtils
    ){
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
    }

    @BeforeAll
    static void initialization(@Autowired CustomerRepository repository,
                            @Value("${spring.mail.port}") int smtpPort,
                            @Value("${spring.mail.host}") String host,
                            @Value("${spring.mail.username}") String username,
                            @Value("${spring.mail.password}") String password){

               CustomerAuthenticationTest.repository = repository;
               customer = new Customer();
               customer.setSurname( "rossi");
               customer.setName("ardemus" );
               customer.setPassword( BCrypt.hashpw(PASSWORD.getBytes(StandardCharsets.UTF_8), BCrypt.gensalt()));
               customer.setEmail(EMAIL);
               customer.setRole(CustomerRole.ADMIN);
               customer.setPhoneNumber("1111111111");
               repository.saveAndFlush(customer);

        ServerSetup serverSetup = new ServerSetup(smtpPort, host, "smtp");
        greenMail = new GreenMail(serverSetup);
        greenMail.withConfiguration(GreenMailConfiguration.aConfig().withUser(username, password));
        greenMail.start();



    }

    @AfterAll
    static void cleanUp(@Autowired CustomerRepository repository){
        repository.delete(customer);

        if (greenMail != null && greenMail.isRunning()) {
            greenMail.stop();
        }
    }


    @Test
    @Order(1)
    void testLoginServiceRightCase(){
        String[] tokens = authenticationService.doLogin(EMAIL, PASSWORD);
        customer = repository.findById(customer.getId()).orElse(null);
        String[] accessInfo = jwtUtils.getTokenAccessId(tokens[1]);
        assertTrue(accessInfo[0].equals(Long.toString(customer.getId())) && accessInfo[1].equals(customer.getRole().toString()));
        String[] refreshInfo = jwtUtils.getTokenRefreshOrPasswordResetInfo(tokens[0]).split( " ");
        assertTrue(refreshInfo[0].equals(Long.toString(customer.getId())) && refreshInfo[1].equals(customer.getTokenRegistration()) && refreshInfo[2].equals(TokenType.REFRESH_TOKEN.toString()));
        refreshToken = tokens[0];
        accessToken = tokens[1];
    }

    @Test
    void testLoginServiceFailures(){

        String wrongEmail ="aaaa@gmail.com";
        Assertions.assertThrows(ResponseStatusException.class,() ->{authenticationService.doLogin(wrongEmail, PASSWORD);});

        String wrongPassword ="32wddmoi32je3";
        Assertions.assertThrows(ResponseStatusException.class,() ->{authenticationService.doLogin(EMAIL, wrongPassword);});

        Assertions.assertThrows(ResponseStatusException.class,() ->{authenticationService.doLogin(wrongEmail, wrongPassword);});
    }

    @Test
    @Order(2)
    void testRefreshTokenRightCase(){
        String token = authenticationService.doRefreshAccessToken(refreshToken);
        String[] accessInfo = jwtUtils.getTokenAccessId(token);
        assertTrue(accessInfo[0].equals(Long.toString(customer.getId())) && accessInfo[1].equals(customer.getRole().toString()));
    }
    @Test
    void testRefreshTokenErrors(){

        //token with no valid id customer
        final String wrongIdCustomerToken = jwtUtils.createRefreshOrPasswordResetToken(UUID.randomUUID().toString(),Long.toString(7L),TokenType.REFRESH_TOKEN);

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class,()->{authenticationService.doRefreshAccessToken(wrongIdCustomerToken);});
        Assertions.assertEquals(HttpStatus.FORBIDDEN,e.getStatusCode());



        String tokenId = UUID.randomUUID().toString();

        //token with no valid session
        final String tokenWithWrongId = jwtUtils.createRefreshOrPasswordResetToken(tokenId,Long.toString(customer.getId()),TokenType.REFRESH_TOKEN);

        e = Assertions.assertThrows(ResponseStatusException.class,()->{authenticationService.doRefreshAccessToken(tokenWithWrongId);});
        Assertions.assertEquals(HttpStatus.FORBIDDEN,e.getStatusCode());

        customer.setTokenRegistration(tokenId);
        repository.saveAndFlush(customer);



        //token password reset for refresh token
        final String tokenWithWrongType = jwtUtils.createRefreshOrPasswordResetToken(tokenId,Long.toString(customer.getId()),TokenType.RESET_PASSWORD);

        e = Assertions.assertThrows(ResponseStatusException.class,()->{authenticationService.doRefreshAccessToken(tokenWithWrongType);});
        Assertions.assertEquals(HttpStatus.FORBIDDEN,e.getStatusCode());



        //token not valid
        final String tokenNotValid = jwtUtils.createRefreshOrPasswordResetToken(tokenId,Long.toString(customer.getId()),
                                                                                TokenType.RESET_PASSWORD).substring(0,tokenWithWrongId.length()/2);

        e = Assertions.assertThrows(ResponseStatusException.class,()->{authenticationService.doRefreshAccessToken(tokenNotValid);});
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED,e.getStatusCode());


    }

    @Test
    void testDoPasswordResetRightCase (){
        String newPassword = "abcd12345";
        String tokenId = UUID.randomUUID().toString();
        String token = jwtUtils.createRefreshOrPasswordResetToken(tokenId,Long.toString(customer.getId()),TokenType.RESET_PASSWORD);
        customer.setTokenRegistration(tokenId);
        repository.saveAndFlush(customer);

        authenticationService.doPasswordReset(token,newPassword);

        customer = repository.findById(customer.getId()).orElse(null);

        assertTrue(BCrypt.checkpw(newPassword,customer.getPassword()));
    }

    @Test
    void testDoPasswordResetErrors (){
        String newPassword = "abcd12345";

        //token with no valid id customer
        final String wrongIdCustomerToken = jwtUtils.createRefreshOrPasswordResetToken(UUID.randomUUID().toString(),Long.toString(7L),TokenType.RESET_PASSWORD);

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class,()->{authenticationService.doPasswordReset(wrongIdCustomerToken,newPassword);});
        Assertions.assertEquals(HttpStatus.FORBIDDEN,e.getStatusCode());



        String tokenId = UUID.randomUUID().toString();

        //token with no valid session
        final String tokenWithWrongId = jwtUtils.createRefreshOrPasswordResetToken(tokenId,Long.toString(customer.getId()),TokenType.RESET_PASSWORD);

        e = Assertions.assertThrows(ResponseStatusException.class,()->{authenticationService.doPasswordReset(tokenWithWrongId,newPassword);});
        Assertions.assertEquals(HttpStatus.FORBIDDEN,e.getStatusCode());

        customer.setTokenRegistration(tokenId);
        repository.saveAndFlush(customer);



        //token password reset for refresh token
        final String tokenWithWrongType = jwtUtils.createRefreshOrPasswordResetToken(tokenId,Long.toString(customer.getId()),TokenType.REFRESH_TOKEN);

        e = Assertions.assertThrows(ResponseStatusException.class,()->{authenticationService.doPasswordReset(tokenWithWrongType,newPassword);});
        Assertions.assertEquals(HttpStatus.FORBIDDEN,e.getStatusCode());



        //token not valid
        final String tokenNotValid = jwtUtils.createRefreshOrPasswordResetToken(tokenId,Long.toString(customer.getId()),
                TokenType.RESET_PASSWORD).substring(0,tokenWithWrongId.length()/2);

        e = Assertions.assertThrows(ResponseStatusException.class,()->{authenticationService.doPasswordReset(tokenNotValid,newPassword);});
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED,e.getStatusCode());


    }

    @Test
    @Order(3)
    void doAuthenticationTestRightCase(){
        Authentication data = authenticationService.doAuthentication(accessToken);
        Assertions.assertEquals(data.getPrincipal(),Long.toString(customer.getId()));
        assertTrue(data.getAuthorities().contains(new SimpleGrantedAuthority(customer.getRole().toString())));
    }

    @Test
    void doAuthenticationTestRightError(){
        String accessTokenRight = jwtUtils.createAccessToken(Long.toString(7L),CustomerRole.ADMIN.toString());
        final String accessTokenError = accessTokenRight.substring(0, accessTokenRight.length()/2);
        Assertions.assertThrows(JWTVerificationException.class,()->{authenticationService.doAuthentication(accessTokenError);});
    }

    @Test
    @Order(5)
    void doEmailPasswordResetTest() throws MessagingException, IOException {
        authenticationService.doEmailPasswordReset(EMAIL);
        greenMail.waitForIncomingEmail(5000, 1);
        customer = repository.findById(customer.getId()).orElse(null);

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();

        Assertions.assertEquals(1, receivedMessages.length);


        MimeMessage receivedMessage = receivedMessages[0];

        Assertions.assertEquals(customer.getEmail(), receivedMessage.getRecipients(MimeMessage.RecipientType.TO)[0].toString());
        Assertions.assertEquals("reset Password", receivedMessage.getSubject());

        String message = GreenMailUtil.getBody(receivedMessage);
        String[] infos =message.split(" ");
        String[] tokenInfo = jwtUtils.getTokenRefreshOrPasswordResetInfo(infos[0]).split(" ");

        Assertions.assertTrue(tokenInfo[0].equals(Long.toString(customer.getId()))
                            && tokenInfo[1].equals(customer.getTokenRegistration())
                            && tokenInfo[2].equals(TokenType.RESET_PASSWORD.toString()));

        Assertions.assertEquals(infos[1], customer.getEmail());


    }

    @Test
    @Order(4)
    void doEmailPasswordResetTestWrongCase() throws MessagingException, IOException {
        String wrongEmail="pippo@gmail.com";
        authenticationService.doEmailPasswordReset(wrongEmail);
        greenMail.waitForIncomingEmail(5000, 1);
        customer = repository.findById(customer.getId()).orElse(null);

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();

        Assertions.assertEquals(0, receivedMessages.length);


    }



}
