package com.example.SiteCercolaFioravante.day.services.impl;

import com.example.SiteCercolaFioravante.day.AgendaDay;
import com.example.SiteCercolaFioravante.day.PeriodType;
import com.example.SiteCercolaFioravante.day.data_transfer_object.CalendarDayDtoList;
import com.example.SiteCercolaFioravante.day.repository.AgendaDayRepository;
import com.example.SiteCercolaFioravante.day.services.DayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DayServiceimpl implements DayService {

    private final AgendaDayRepository repository;

    @Override
    public boolean insertPeriodInactivity(LocalDate start, LocalDate end) {

        if(repository.getOverlapping(start,end) > 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"non puoi inserire un periodo che si sovrappone con altri");

        AgendaDay period = new AgendaDay();
        period.setAvailable(false);
        period.setStartDay(start);
        period.setEndDay(end);
        period.setPeriodType(PeriodType.PERIOD);

        repository.save(period);

        return true;
    }

    @Override
    public boolean insertDayInactivity(LocalDate start) {
        if(repository.getOverlapping(start,start) > 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"non puoi inserire un periodo che si sovrappone con altri");

        AgendaDay period = new AgendaDay();
        period.setAvailable(false);
        period.setStartDay(start);
        period.setEndDay(start);
        period.setPeriodType(PeriodType.DAY);

        repository.save(period);

        return true;
    }

    @Override
    public CalendarDayDtoList getDayFromPeriod(LocalDate start, LocalDate end) {
        return null;
    }
}
