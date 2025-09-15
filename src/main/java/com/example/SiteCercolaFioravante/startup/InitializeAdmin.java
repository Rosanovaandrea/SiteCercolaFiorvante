package com.example.SiteCercolaFioravante.startup;

import com.example.SiteCercolaFioravante.customer.Credentials;
import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.CustomerRole;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.example.SiteCercolaFioravante.customer.services.CustomerService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class InitializeAdmin {
    private final CustomerRepository repository;
    private final String name;
    private final String surname;
    private final String email;
    private final String password;
    private final String number;

    public InitializeAdmin(@Autowired CustomerRepository repository,
                           @Value("${admin.name}") String name,
                           @Value("${admin.surname}")String surname,
                           @Value("${admin.email}")String email,
                           @Value("${admin.password}")String password,
                           @Value("${admin.number}")String number) {
        this.repository = repository;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.number = number;
    }

    @PostConstruct
    public void initAdmin(){
        if(repository.findCustomerByRole(CustomerRole.ADMIN).isEmpty()){
            Customer customer = new Customer();
            customer.setName(name);
            customer.setSurname(surname);
            Credentials credentials = new Credentials();
            credentials.setEmail(email);
            credentials.setPassword(BCrypt.hashpw(password,BCrypt.gensalt()));
            credentials.setCustomer(customer);
            customer.setCredentials(credentials);
            customer.setPhoneNumber(number);
            customer.setRole(CustomerRole.ADMIN);

            repository.saveAndFlush(customer);
        }
    }


}
