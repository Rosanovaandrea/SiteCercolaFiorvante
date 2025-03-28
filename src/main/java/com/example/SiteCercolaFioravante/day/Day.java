package com.example.SiteCercolaFioravante.day;

import com.example.SiteCercolaFioravante.reservation.Reservation;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;


@Entity
@Data
@NoArgsConstructor
public class Day {

    @Id
    private Date date;

    @ColumnDefault( "true" )
    private boolean isAvailable;

    @Array( length = 8 )
    ArrayList<Integer> occupiedHour;

    @OneToMany( mappedBy = "day", cascade = CascadeType.ALL )
    private LinkedList<Reservation> reservations;


}
