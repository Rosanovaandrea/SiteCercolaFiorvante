package com.example.SiteCercolaFioravante.day.services;

import com.example.SiteCercolaFioravante.day.data_transfer_object.CalendarDayDtoList;
import com.example.SiteCercolaFioravante.day.data_transfer_object.CalendarDtoSingleComplete;
import com.example.SiteCercolaFioravante.day.Day;
import com.example.SiteCercolaFioravante.reservation.Reservation;

import java.time.LocalDate;
import java.util.LinkedList;

public interface DayService {
        Day insertDayFromReservation(LocalDate date, Reservation reservation);
        boolean insertDay(CalendarDtoSingleComplete Day);
        LinkedList<CalendarDayDtoList> getDaysFromMonth(LocalDate start);
        boolean ModifyDay(CalendarDtoSingleComplete day);
        void deleteReservationFromDay(LocalDate date,int hour);
}
