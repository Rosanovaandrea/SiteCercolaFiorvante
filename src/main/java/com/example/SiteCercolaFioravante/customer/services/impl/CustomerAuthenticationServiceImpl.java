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
public class CustomerAuthenticationServiceImpl implements CustomerAuthenticationService {

    private final JwtUtils jwtUtils;
    private final CustomerRepository repository;
    private final MapperCustomer mapper;
    private final JavaMailSender emailSender;


    @Override
    @Transactional
    public boolean doEmailPasswordReset(String email) {

        Customer customer = repository.findCustomerByEmail(email).orElse(null);
        if(customer!=null) {
            String tokenId = UUID.randomUUID().toString();
            String tokenString = jwtUtils.createRefreshOrPasswordResetToken(tokenId,Long.toString(customer.getId()), TokenType.RESET_PASSWORD);
            customer.setTokenRegistration(tokenId);
            repository.save(customer);
            sendMessageEmail(email, "reset Password", tokenString + " " + email);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean doPasswordReset( String token, String password) {

        String[] info;

        try {
            info = jwtUtils.getTokenRefreshOrPasswordResetInfo(token).split(" ");



        Customer customer = repository.findById(Long.parseLong(info[0])).orElse(null);

        if(customer == null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Utente non registrato");
        }
        if(!customer.getTokenRegistration().equals(info[1]) || !info[2].equals(TokenType.RESET_PASSWORD.toString())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Link non valido");
        }


        customer.setPassword(BCrypt.hashpw(password.getBytes(StandardCharsets.UTF_8), BCrypt.gensalt()));
        customer.setTokenRegistration(null);

        repository.saveAndFlush(customer);
        return true;

        }catch(JWTVerificationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Link non valido");
        }


    }

    @Override
    public String[] doLogin(String email, String password) {


        Optional<Customer> opt = repository.findCustomerByEmail(email);

        if(opt.isEmpty())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "password o email non valida");


        String passwordFromDB = opt.get().getPassword();

        if (BCrypt.checkpw(password, passwordFromDB)){

            String idRegistrationToken = UUID.randomUUID().toString();

            opt.get().setTokenRegistration(idRegistrationToken);
            repository.saveAndFlush(opt.get());



            return new String[] {jwtUtils.createRefreshOrPasswordResetToken(idRegistrationToken,Long.toString(opt.get().getId()),TokenType.REFRESH_TOKEN),
                                jwtUtils.createAccessToken(Long.toString(opt.get().getId()),opt.get().getRole().toString())};

            }
        else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"password o email non valida");

    }

    @Override
    @Transactional
    public String[] doRegistration(CustomerDtoComplete customer) {

            Customer customerDB = mapper.fromDtoCompleteToCustomer(customer);
            customerDB.setRole(CustomerRole.CUSTOMER);

            String password = BCrypt.hashpw(customer.password(), BCrypt.gensalt());

            customerDB.setPassword(password);

            String idRegistrationToken = UUID.randomUUID().toString();

            customerDB.setTokenRegistration(idRegistrationToken);

            repository.save(customerDB);
            repository.flush();

        return new String[] {jwtUtils.createRefreshOrPasswordResetToken(idRegistrationToken,Long.toString(customerDB.getId()),TokenType.REFRESH_TOKEN),
                jwtUtils.createAccessToken(Long.toString(customerDB.getId()),customerDB.getRole().toString())};

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

        Customer customer = null;
        String[] info;

        try{
        info = jwtUtils.getTokenRefreshOrPasswordResetInfo(token).split(" ");


        customer = repository.findById(Long.parseLong(info[0])).orElse(null);

        if(
                customer == null ||
                customer.getTokenRegistration() == null ||
                !customer.getTokenRegistration().equals(info[1]) ||
                !info[2].equals(TokenType.REFRESH_TOKEN.toString())
        ) {

            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"refresh token non valido");

            }

        return jwtUtils.createAccessToken(Long.toString(customer.getId()),customer.getRole().toString());

        }catch(JWTVerificationException e){
            System.err.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "refresh token non valido");

        }

    }



    @Override
    public Authentication doAuthentication(String token) {
        String[] info = jwtUtils.getTokenAccessId(token);
        List<SimpleGrantedAuthority> authorities = new LinkedList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(info[1]));
        return new UsernamePasswordAuthenticationToken(info[0],token, authorities);
    }
}
