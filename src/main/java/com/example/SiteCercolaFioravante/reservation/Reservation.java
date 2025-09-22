package com.example.SiteCercolaFioravante.reservation;


import com.example.SiteCercolaFioravante.day.AgendaDay;
import com.example.SiteCercolaFioravante.service.Service;
import com.example.SiteCercolaFioravante.customer.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint( columnNames = { "agenda_day_id", "hour_reserved" } ) } )
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn( name = "service_id", nullable = false )
    private Service service;

    @Version
    @ColumnDefault("0")
    private long version;

    @ManyToOne
    @JoinColumn( name = "customer_id", nullable = false )
    private Customer customer;

    @ManyToOne
    @JoinColumn( name = "agenda_day_id", nullable = false )
    private AgendaDay agendaDay;

    @ColumnDefault( "false" )
    private boolean isCompleted;

    @ColumnDefault( "true" )
    private boolean isDeletable;

    @Min( 0 )
    @Max( 7 )
    private int hourReserved;


}
