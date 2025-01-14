package com.example.SiteCercolaFioravante.reservation;

import com.example.SiteCercolaFioravante.day.Day;
import com.example.SiteCercolaFioravante.service.Service;
import com.example.SiteCercolaFioravante.user.User;
import lombok.Data;

import java.io.Serializable;


@Data
public class CompositeKeyReservation implements Serializable {
    private Service service;
    private User user;
    private Day day;
}
