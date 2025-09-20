package com.example.SiteCercolaFioravante.day;

import com.example.SiteCercolaFioravante.reservation.Reservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(name = "\"day\"")
public class Day {

    @Id
    private long id;

    @NotNull
    private LocalDate start;

    @NotNull
    private LocalDate end;

    @NotNull
    private PeriodType periodType;

    @ColumnDefault( "true" )
    private boolean isAvailable;

    @Version
    @ColumnDefault("0")
    private long version;

    @Size( max = 8 )
    @ElementCollection
    Set<Integer> occupiedHour;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "day", cascade = CascadeType.ALL )
    private List<Reservation> reservations;


}
