package com.example.SiteCercolaFioravante.day;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Entity
@Data
@AllArgsConstructor
public class Day {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @Column(nullable = false)
    boolean[] hours;

    @Column(nullable = false)
    Date date;

    public Day() {
        hours = new boolean[8];
        date = new Date();
    }
}
