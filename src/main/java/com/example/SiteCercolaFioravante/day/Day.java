package com.example.SiteCercolaFioravante.day;

import com.example.SiteCercolaFioravante.reservation.Reservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
public class Day {

    @Id
    private LocalDate date;

    @ColumnDefault( "false" )
    private boolean isAvailable;

    @Size( max = 8 )
    @ElementCollection
    Set<Integer> occupiedHour;

    @OneToMany( mappedBy = "day", cascade = CascadeType.ALL )
    private List<Reservation> reservations;


}
