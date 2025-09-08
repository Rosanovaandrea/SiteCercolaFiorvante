package com.example.SiteCercolaFioravante.customer;
import com.example.SiteCercolaFioravante.reservation.Reservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column( nullable = false )
    private String surname;

    @NotNull
    @Column( nullable = false )
    private String name;


    @OneToOne(mappedBy = "customer") // Relazione definita in Customer
    @JoinColumns({@JoinColumn(name = "credentials_id")})
    private Credentials credentials;

    @NotNull
    @Column( nullable = false )
    @Enumerated(EnumType.STRING)
    private CustomerRole role;

    private String tokenRegistration;

    @NotNull
    @Size(max = 10, min = 10)
    @Column(name = "phone_number", length = 10, unique = true)
    private String phoneNumber;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL )
    private List<Reservation> reservations;


}
