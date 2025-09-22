package com.example.SiteCercolaFioravante.day.services;

import com.example.SiteCercolaFioravante.day.data_transfer_object.CalendarDayDtoList;

import java.time.LocalDate;

public interface DayService {
  boolean insertPeriodInactivity(LocalDate start , LocalDate end);
  boolean insertDayInactivity(LocalDate startDay);
  CalendarDayDtoList getDayFromPeriod(LocalDate start, LocalDate end);
}
