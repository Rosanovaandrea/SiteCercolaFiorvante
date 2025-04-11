package com.example.SiteCercolaFioravante.day.services;

import com.example.SiteCercolaFioravante.day.Day;
import com.example.SiteCercolaFioravante.day.data_transfer_object.CalendarDayDtoList;
import com.example.SiteCercolaFioravante.reservation.Reservation;

import java.time.LocalDate;
import java.util.LinkedList;

public interface DayService {
        void insertReservationInDay(Reservation reservation);
        boolean insertDayFalse(LocalDate date);
        LinkedList<CalendarDayDtoList> getDaysFromMonth(LocalDate start);
        boolean deleteDay(LocalDate date);
        void deleteReservationFromDay(LocalDate date,int hour);
        Day insertReservationfromReservation(LocalDate date);
}
