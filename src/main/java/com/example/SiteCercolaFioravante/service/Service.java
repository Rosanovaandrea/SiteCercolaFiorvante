package com.example.SiteCercolaFioravante.service;

import com.example.SiteCercolaFioravante.reservation.Reservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.ColumnDefault;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(unique = true)
    private String serviceName;

    @ElementCollection(fetch = FetchType.EAGER)
    @Size( max = 4 )
    private Set<String> images;

    @Version
    @ColumnDefault("0")
    private long version;

    private String firstImage;

    @Column(nullable = false)
    private double price;

    @NotNull
    @Column( nullable = false, length = 1500 )
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "service" )
    private List<Reservation> reservations;


}
