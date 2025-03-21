package com.example.SiteCercolaFioravante.day.services.impl;

import com.example.SiteCercolaFioravante.day.data_transfer_object.CalendarDayDtoList;
import com.example.SiteCercolaFioravante.day.data_transfer_object.CalendarDtoSingleComplete;
import com.example.SiteCercolaFioravante.day.data_transfer_object.MapperDay;
import com.example.SiteCercolaFioravante.day.Day;
import com.example.SiteCercolaFioravante.day.repository.DayRepository;
import com.example.SiteCercolaFioravante.day.services.DayService;
import com.example.SiteCercolaFioravante.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;

@Service
@RequiredArgsConstructor
public class DayServiceimpl implements DayService {

    private final DayRepository repository;
    private final MapperDay mapper;

    @Override
    public Day insertDayFromReservation(CalendarDtoSingleComplete day, Reservation reservation) {
      Day dayDB =new Day();
      mapper.DayDtoCompleteToDay(day,dayDB);
      LinkedList<Reservation> reservations = new LinkedList<Reservation>();
      reservations.add(reservation);
      dayDB.setReservations(reservations);
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
    public LinkedList<CalendarDayDtoList> getDaysFromMonth(Date start) {

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(start);
        calendar.add(Calendar.DATE,30);

        Date end = calendar.getTime();

        return new LinkedList<CalendarDayDtoList>( repository.getDaysByTime(start,end));
    }

    @Override
    public boolean ModifyDay(CalendarDtoSingleComplete day) {
        Day dayDB =new Day();
        mapper.DayDtoCompleteToDay(day,dayDB);
        repository.save(dayDB);
        return true;
    }
}
