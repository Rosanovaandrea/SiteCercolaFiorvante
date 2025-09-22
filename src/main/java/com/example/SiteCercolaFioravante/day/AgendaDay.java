package com.example.SiteCercolaFioravante.day;

import com.example.SiteCercolaFioravante.reservation.Reservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(name = "agenda_day",uniqueConstraints = { @UniqueConstraint( columnNames = { "start_day", "end_day" } ) } )
public class AgendaDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column( nullable = false )
    @Temporal(TemporalType.DATE)
    private LocalDate startDay;

    @NotNull
    @Column( nullable = false )
    @Temporal(TemporalType.DATE)
    private LocalDate endDay;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column( nullable = false )
    private PeriodType periodType;

    @ColumnDefault( "true" )
    private boolean isAvailable;

    @Version
    @ColumnDefault("0")
    private long version;

    @Size( max = 8 )
    @ElementCollection
    Set<Integer> occupiedHour;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agendaDay", cascade = CascadeType.ALL )
    private List<Reservation> reservations;


}
