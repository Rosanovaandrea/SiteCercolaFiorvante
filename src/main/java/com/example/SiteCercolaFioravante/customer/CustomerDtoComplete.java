package com.example.SiteCercolaFioravante.customer;

import com.example.SiteCercolaFioravante.reservation.Reservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.LinkedList;

public record CustomerDtoComplete(
       long id,
       String surname,
       String name,
       String password,
       String email,
       String role,
       long phoneNumber,
       LinkedList<Reservation> reservations) {
}
