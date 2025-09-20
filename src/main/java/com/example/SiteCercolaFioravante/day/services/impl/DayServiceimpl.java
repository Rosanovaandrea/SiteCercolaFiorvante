package com.example.SiteCercolaFioravante.day.services.impl;

import com.example.SiteCercolaFioravante.day.Day;
import com.example.SiteCercolaFioravante.day.repository.DayRepository;
import com.example.SiteCercolaFioravante.day.services.DayService;
import com.example.SiteCercolaFioravante.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;

@Service
@RequiredArgsConstructor
public class DayServiceimpl implements DayService {

    private final DayRepository repository;

    @Transactional
    @Override
    public void insertReservationInDay(Reservation reservation) {

       Day dayDB =reservation.getDay();

      if(dayDB.getReservations() == null)
          dayDB.setReservations(new LinkedList<Reservation>());

      if(dayDB.getOccupiedHour() == null)
          dayDB.setOccupiedHour(new HashSet<Integer>());

      if( dayDB.getOccupiedHour().contains(reservation.getHour())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "non puoi inserire una prenotazione in un'ora già occupata");


      dayDB.getReservations().add(reservation);
      dayDB.getOccupiedHour().add(reservation.getHour());

      repository.save(dayDB);
    }

    @Transactional
    @Override
    public boolean insertDayFalse(LocalDate date) {
        Day dayDB =new Day();
        dayDB.setDate(date);
        repository.save(dayDB);
        return true;
    }

    @Override
    public LinkedList<CalendarDayDtoList> getDaysFromMonth(LocalDate start) {
        LocalDate end = start.plusMonths(1L);
        return new LinkedList<CalendarDayDtoList>( repository.getDaysByTime(start,end));
    }

    @Transactional
    @Override
    public boolean deleteDay(LocalDate date) {
       Day day = repository.getSingleDayDB(date);
       if(day.getReservations() != null){
           repository.delete(day);
           return true;
       }
           throw new IllegalArgumentException();
    }


    @Transactional
    @Override
    public void deleteReservationFromDay(LocalDate date, int hour) {
        Day dayDB = repository.getSingleDayDB(date);
        if(dayDB == null || !dayDB.getOccupiedHour().contains(hour)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"data e ora non valida");
        dayDB.getOccupiedHour().remove(hour);
        repository.saveAndFlush(dayDB);
    }

    @Override
    public Day insertReservationfromReservation(LocalDate date) {
        Day dayDB = repository.getSingleDayDB(date);

        if(dayDB == null) dayDB = new Day();

        dayDB.setDate(date);
        dayDB.setAvailable(true);
        repository.saveAndFlush(dayDB);
        return dayDB;
    }
}
