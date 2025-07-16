package com.example.SiteCercolaFioravante.integration_service_tests;

import com.example.SiteCercolaFioravante.customer.CustomerRole;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmailSenderTest {

    private final JavaMailSender javaMailSender;


    public EmailSenderTest(@Autowired JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @Test
    @Order(1)
    void emailSenderTest1() {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("ardemusfrizzo@gmail.com");
            helper.setTo("rosanovaandrea82@gmail.com");
            helper.setSubject("email di prova");
            helper.setText("ciao");

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


}