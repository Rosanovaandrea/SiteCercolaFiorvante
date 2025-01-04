package com.example.SiteCercolaFioravante.service;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.Text;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Service {

    @Id
    @Column(nullable = false)
    private String name;

    private String[] images;

    @Column(nullable = false, length = 1500)
    private String description;


}
