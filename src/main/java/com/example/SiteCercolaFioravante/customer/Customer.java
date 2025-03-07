package com.example.SiteCercolaFioravante.customer;
import com.example.SiteCercolaFioravante.reservation.Reservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;

@Data
@NoArgsConstructor
@Entity
@Table(name="customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull
    @Column( nullable = false )
    private String surname;

    @NotNull
    @Column( nullable = false )
    private String name;

    @NotNull
    @Column(nullable = false)
    private String password;

    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    @Column( nullable = false )
    private String role;

    private String tokenRegistration;

    @NotNull
    @Column( length = 10, unique = true)
    private String phoneNumber;

    @OneToMany( mappedBy = "customer", cascade = CascadeType.ALL )
    private LinkedList<Reservation> reservations;


}
