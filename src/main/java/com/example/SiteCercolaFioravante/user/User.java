package com.example.SiteCercolaFioravante.user;
import com.example.SiteCercolaFioravante.reservation.Reservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;

@Data
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE )
    private Long id;

    @NotNull
    @Column( nullable = false )
    private String surname;

    @NotNull
    @Column( nullable = false )
    private String name;

    @Column( unique = true )
    private String email;

    @NotNull
    @Column( nullable = false )
    private String role;

    @NotNull
    @Column( nullable = false, length = 10)
    private int phoneNumber;

    @OneToMany( mappedBy = "user", cascade = CascadeType.ALL )
    private LinkedList<Reservation> reservations;


}
