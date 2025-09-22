package com.example.SiteCercolaFioravante.day.data_transfer_object;

import com.example.SiteCercolaFioravante.day.PeriodType;

import java.time.LocalDate;
import java.util.HashSet;

public interface CalendarDayDtoList {
    long getId();
    LocalDate getStartDay();
    LocalDate getEndDay();
    PeriodType getPeriodType();
    boolean getIsAvailable();
    HashSet<Integer> getOccupiedHour();
}
