package com.example.SiteCercolaFioravante.customer.services.impl;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.SiteCercolaFioravante.TokenType;
import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.CustomerRole;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.MapperCustomer;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.example.SiteCercolaFioravante.customer.services.CustomerAuthenticationService;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoComplete;
import com.example.SiteCercolaFioravante.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.*;


@RequiredArgsConstructor
@Service
@Slf4j
public class CustomerAuthenticationServiceImpl implements CustomerAuthenticationService {

    private final AuthenticationStaticLibraryWrapper wrapper;
    private final JwtUtils jwtUtils;
    private final CustomerRepository repository;
    private final MapperCustomer mapper;
    private final JavaMailSender emailSender;


    @Override
    @Transactional
    public boolean doEmailPasswordReset(String email) {

        Customer customer = repository.findCustomerByEmail(email).orElse(null);
        if(customer!=null) {
            String tokenId = wrapper.getUUID().toString();
            String tokenString = jwtUtils.createResetPasswordToken(tokenId,Long.toString(customer.getId()));
            customer.setTokenRegistration(tokenId);
            repository.save(customer);
            sendMessageEmail(email, "reset Password", tokenString + " " + email);
        }else{
            log.warn("password reset not produced for not present customer");
        }
        return true;
    }

    @Override
    @Transactional
    public boolean doPasswordReset( String token, String password) {

        String[] info;

        try {

        info = jwtUtils.passwordResetJwtVerification(token);

        }catch(JWTVerificationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Link non valido");
        }

        Customer customer = repository.findById(Long.parseLong(info[0])).orElse(null);

        if(customer == null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Utente non registrato");
        }

        if(customer.getTokenRegistration() == null || !customer.getTokenRegistration().equals(info[1])){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Link non valido");
        }

        customer.setPassword(wrapper.setPassword(password));
        customer.setTokenRegistration(null);
        repository.saveAndFlush(customer);

        return true;

    }

    @Override
    public String[] doLogin(String email, String password) {


       Customer customer = repository.findCustomerByEmail(email).orElse(null);

        if(customer == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "password o email non valida");


        String passwordFromDB = customer.getPassword();

        if (!wrapper.checkPassword(password, passwordFromDB)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"password o email non valida");
        }


            String idRegistrationToken = wrapper.getUUID().toString();

            customer.setTokenRegistration(idRegistrationToken);
            repository.saveAndFlush(customer);



            return new String[] {
                    jwtUtils.createRefreshToken(idRegistrationToken,Long.toString(customer.getId())),
                    jwtUtils.createAccessToken(Long.toString(customer.getId()),customer.getRole().toString())
            };




    }

    @Override
    @Transactional
    public String[] doRegistration(CustomerDtoComplete customer) {

            Customer customerDB = mapper.fromDtoCompleteToCustomer(customer);
            customerDB.setRole(CustomerRole.CUSTOMER);

            String password = wrapper.setPassword(customer.password());

            customerDB.setPassword(password);

            String idRegistrationToken = UUID.randomUUID().toString();

            customerDB.setTokenRegistration(idRegistrationToken);

            repository.save(customerDB);
            repository.flush();

        return new String[] {
                jwtUtils.createRefreshToken(idRegistrationToken,Long.toString(customerDB.getId())),
                jwtUtils.createAccessToken(Long.toString(customerDB.getId()),customerDB.getRole().toString())
        };

    }

    private void sendMessageEmail(String to,String subject,String text){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("ardemusfrizzo@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        emailSender.send(message);
    }





    @Override
    public String doRefreshAccessToken(String token) {

        String[] info;

        try{
            info = jwtUtils.refreshTokenVerification(token);

        }catch(JWTVerificationException e){
            log.warn("si è provato a fare un accesso con token non valido");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "refresh token non valido");
        }

        Customer customer = repository.findById(Long.parseLong(info[0])).orElse(null);

        if( customer == null || customer.getTokenRegistration() == null || !customer.getTokenRegistration().equals( info[1] ) ) {
            log.warn("si è provato a fare un accesso con token non memorizzato ");
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, "refresh token non valido" );
        }

        return jwtUtils.createAccessToken(  Long.toString( customer.getId() ),  customer.getRole().toString() );

    }



    @Override
    public Authentication doAuthentication(String token) {
        String[] info = jwtUtils.getTokenAccessId(token);
        List<SimpleGrantedAuthority> authorities = new LinkedList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(info[1]));
        return new UsernamePasswordAuthenticationToken(info[0],token, authorities);
    }
}
