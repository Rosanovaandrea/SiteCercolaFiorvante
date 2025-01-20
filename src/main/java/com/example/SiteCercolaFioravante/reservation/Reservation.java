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
import org.springframework.boot.autoconfigure.web.WebProperties;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint( columnNames = { "day", "hour" } ) } )
@Data
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    @JoinColumn( name = "service_id", nullable = false )
    private Service service;

    @ManyToOne
    @JoinColumn( name = "customer_id", nullable = false )
    private Customer customer;

    @ManyToOne
    @JoinColumn( name = "day_date", nullable = false )
    private Day day;

    @ColumnDefault( "false" )
    private boolean isCompleted;

    @ColumnDefault( "true" )
    private boolean isDeletable;

    @Min( 0 )
    @Max( 7 )
    private int hour;


}
