package com.example.SiteCercolaFioravante.customer;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
