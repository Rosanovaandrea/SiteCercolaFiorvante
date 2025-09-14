package com.example.SiteCercolaFioravante.customer.services.impl;

import com.auth0.jwt.exceptions.JWTVerificationException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
            repository.saveAndFlush(customer);
            String subject ="reset Password";
            sendMessageEmail(email, subject, tokenString + " " + email);
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

        customer.getCredentials().setPassword(wrapper.setPassword(password));
        customer.setTokenRegistration(null);
        repository.saveAndFlush(customer);

        return true;

    }

    @Override
    @Transactional
    public String[] doLogin(String email, String password) {


       Customer customer = repository.findCustomerByEmail(email).orElse(null);

        if(customer == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "password o customerId non valida");


        String passwordFromDB = customer.getCredentials().getPassword();

        if (!wrapper.checkPassword(password, passwordFromDB)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"password o customerId non valida");
        }


            String idRegistrationToken = wrapper.getUUID().toString();

            customer.setTokenRegistration(idRegistrationToken);
            repository.saveAndFlush(customer);



            return new String[] {
                    jwtUtils.createRefreshToken(idRegistrationToken,Long.toString(customer.getId())),
                    jwtUtils.createAccessToken(Long.toString(customer.getId()),customer.getRole().toString())
            };

    }

    public void sendMessageEmail(String to,String subject,String text){
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

    @Transactional
    @Override
    public boolean doLogout(String refreshToken){
        String[] info;
        try {
            info = jwtUtils.refreshTokenVerification(refreshToken);
        }catch (JWTVerificationException e){
            log.warn("logout tentato con token invalido"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"token non valido");
        }

        Customer customer = repository.findById(Long.parseLong(info[0])).orElse(null);

        if( customer == null || customer.getTokenRegistration() == null || !customer.getTokenRegistration().equals( info[1] ) ) {
            log.warn("si è provato a fare un logout con token non memorizzato ");
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, "refresh token non valido" );
        }

        customer.setTokenRegistration(null);

        repository.save(customer);

        return true;
    }
}
