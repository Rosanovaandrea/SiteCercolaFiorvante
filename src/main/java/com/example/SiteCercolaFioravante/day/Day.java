package com.example.SiteCercolaFioravante.day;

import com.example.SiteCercolaFioravante.reservation.Reservation;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.LinkedList;

@Getter
@Setter
@Entity
@Data
@AllArgsConstructor


public class Day {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE )
    private long id;

    @Column( nullable = false, unique = true )
    private Date date;

    @OneToMany( mappedBy = "day", cascade = CascadeType.ALL )
    private LinkedList<Reservation> reservations;


}
