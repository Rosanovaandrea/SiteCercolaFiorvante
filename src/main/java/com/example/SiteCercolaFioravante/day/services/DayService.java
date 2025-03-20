package com.example.SiteCercolaFioravante.day.services;

import com.example.SiteCercolaFioravante.day.Data_transfer_object.CalendarDayDtoList;
import com.example.SiteCercolaFioravante.day.Data_transfer_object.CalendarDtoSingleComplete;
import com.example.SiteCercolaFioravante.day.Day;
import com.example.SiteCercolaFioravante.reservation.Reservation;

import java.util.Date;
import java.util.LinkedList;

public interface DayService {
        Day insertDayFromReservation(CalendarDtoSingleComplete Day, Reservation reservation);
        boolean insertDay(CalendarDtoSingleComplete Day);
        LinkedList<CalendarDayDtoList> getDaysFromMonth(Date start);
        boolean ModifyDay(CalendarDtoSingleComplete day);
}
