package com.example.SiteCercolaFioravante.day.repository;

import com.example.SiteCercolaFioravante.reservation.Reservation;

import java.util.Date;
import java.util.LinkedList;

public interface CalendarDayProjection {
     Date getDate();
     Boolean[] getOccupiedHour();
}
