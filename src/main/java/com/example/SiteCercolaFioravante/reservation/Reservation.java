package com.example.SiteCercolaFioravante.reservation;


import com.example.SiteCercolaFioravante.day.Day;
import com.example.SiteCercolaFioravante.service.Service;
import com.example.SiteCercolaFioravante.customer.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint( columnNames = { "day", "hour" } ) } )
@Data
@NoArgsConstructor
@IdClass(CompositeKeyReservation.class)
public class Reservation {

    @Id
    @ManyToOne
    @JoinColumn( name = "service_id", nullable = false )
    private Service service;

    @Id
    @ManyToOne
    @JoinColumn( name = "customer_id", nullable = false )
    private Customer customer;

    @Id
    @ManyToOne
    @JoinColumn( name = "day_id", nullable = false )
    private Day day;

    @ColumnDefault( "false" )
    private boolean isCompleted;

    @ColumnDefault( "true" )
    private boolean isDeletable;

    @Min( 0 )
    @Max( 7 )
    private int hour;


}
