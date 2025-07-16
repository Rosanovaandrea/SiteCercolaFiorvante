package com.example.SiteCercolaFioravante.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailSender {

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String auth;
    private final String starttls;

    public EmailSender(@Value("${spring.mail.host}") String host,
                       @Value("${spring.mail.port}") int port,
                       @Value("${spring.mail.username}") String username,
                       @Value("${spring.mail.password}") String password,
                       @Value("${spring.mail.properties.mail.smtp.auth}") String auth,
                       @Value("${spring.mail.properties.mail.smtp.starttls.enable}") String starttls){

        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.auth = auth;
        this.starttls = starttls;

    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.ssl.enable", "false");
        props.put("mail.debug", "true");



        return mailSender;
    }
}
