package com.example.SiteCercolaFioravante.day.services.impl;

import com.example.SiteCercolaFioravante.day.data_transfer_object.CalendarDayDtoList;
import com.example.SiteCercolaFioravante.day.data_transfer_object.CalendarDtoSingleComplete;
import com.example.SiteCercolaFioravante.day.data_transfer_object.MapperDay;
import com.example.SiteCercolaFioravante.day.Day;
import com.example.SiteCercolaFioravante.day.repository.DayRepository;
import com.example.SiteCercolaFioravante.day.services.DayService;
import com.example.SiteCercolaFioravante.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

@Service
@RequiredArgsConstructor
public class DayServiceimpl implements DayService {

    private final DayRepository repository;
    private final MapperDay mapper;

    @Override
    public Day insertDayFromReservation(LocalDate date, Reservation reservation) {
       Day dayDB =repository.getSingleDayDB(date);


       if(dayDB != null){

           if(dayDB.getOccupiedHour().contains(reservation.getHour()))
               throw new RuntimeException();

           dayDB.getOccupiedHour().add(reservation.getHour());
           dayDB.getReservations().add(reservation);
           repository.save(dayDB);
           return dayDB;
       }

       dayDB=new Day();

      dayDB.setDate(date);
      dayDB.setAvailable(true);

      LinkedList<Reservation> reservations = new LinkedList<Reservation>();
      reservations.add(reservation);

      dayDB.setReservations(reservations);
      dayDB.getOccupiedHour().add(reservation.getHour());

      repository.save(dayDB);
      return dayDB;
    }

    @Override
    public boolean insertDay(CalendarDtoSingleComplete day) {
        Day dayDB =new Day();
        mapper.DayDtoCompleteToDay(day,dayDB);
        repository.save(dayDB);
        return true;
    }

    @Override
    public LinkedList<CalendarDayDtoList> getDaysFromMonth(LocalDate start) {
        LocalDate end = start.plusMonths(1L);
        return new LinkedList<CalendarDayDtoList>( repository.getDaysByTime(start,end));
    }

    @Override
    public boolean ModifyDay(CalendarDtoSingleComplete day) {
        Day dayDB =new Day();
        mapper.DayDtoCompleteToDay(day,dayDB);
        repository.save(dayDB);
        return true;
    }

    @Override
    public void deleteReservationFromDay(LocalDate date, int hour) {
        Day dayDB = repository.getSingleDayDB(date);
        dayDB.getOccupiedHour().remove(hour);
        repository.saveAndFlush(dayDB);
    }
}
