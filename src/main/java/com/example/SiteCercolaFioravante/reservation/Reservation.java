package com.example.SiteCercolaFioravante.reservation;


import com.example.SiteCercolaFioravante.day.Day;
import com.example.SiteCercolaFioravante.service.Service;
import com.example.SiteCercolaFioravante.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private Service service;

    private User user;

    private Day day;


}
