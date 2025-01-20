package com.example.SiteCercolaFioravante.service;

import com.example.SiteCercolaFioravante.reservation.Reservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Array;

import java.util.LinkedList;

@Data
@NoArgsConstructor
@Entity
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull
    @Column(nullable = false)
    private String serviceName;

    @Array( length = 4 )
    private String[] images;

    @NotNull
    @Column( nullable = false, length = 1500 )
    private String description;

    @OneToMany( mappedBy = "service" )
    private LinkedList<Reservation> reservations;

    @Max(10)
    @Min(0)
    private int preference;

    private int position;


}
