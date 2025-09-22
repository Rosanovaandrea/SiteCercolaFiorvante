package com.example.SiteCercolaFioravante.day.services.impl;

import com.example.SiteCercolaFioravante.day.AgendaDay;
import com.example.SiteCercolaFioravante.day.PeriodType;
import com.example.SiteCercolaFioravante.day.repository.AgendaDayRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class DayServiceimplTest {

    @Mock
    private AgendaDayRepository repository;

    @InjectMocks
    private DayServiceimpl service;

    @Captor
    private ArgumentCaptor<AgendaDay> dayArgumentCaptor;


    @Test
    void insertPeriodInactivityRightTest() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.minusDays(1);
        Mockito.when(repository.save(Mockito.any(AgendaDay.class))).thenReturn(Mockito.mock(AgendaDay.class));
        Mockito.when(repository.getOverlapping(start,end)).thenReturn(0);

        boolean result = service.insertPeriodInactivity(start,end);

        Assertions.assertTrue(result);

        Mockito.verify(repository,Mockito.times(1)).save(dayArgumentCaptor.capture());
         AgendaDay day = dayArgumentCaptor.getValue();

        Assertions.assertEquals(end,day.getEndDay());
        Assertions.assertEquals(start,day.getStartDay());
        Assertions.assertFalse(day.isAvailable());
        Assertions.assertEquals(PeriodType.PERIOD,day.getPeriodType());
        Assertions.assertNull(day.getOccupiedHour());
        Assertions.assertNull(day.getReservations());

    }

    @Test
    void insertPeriodInactivityErrorOverlappingTest() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.minusDays(1);
        Mockito.when(repository.getOverlapping(start,end)).thenReturn(1);

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class,()-> service.insertPeriodInactivity(start,end));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST,e.getStatusCode());
    }

    @Test
    void insertDayInactivityRightTest() {

        LocalDate start = LocalDate.now();
        Mockito.when(repository.save(Mockito.any(AgendaDay.class))).thenReturn(Mockito.mock(AgendaDay.class));
        Mockito.when(repository.getOverlapping(start,start)).thenReturn(0);

        boolean result = service.insertDayInactivity(start);

        Assertions.assertTrue(result);

        Mockito.verify(repository,Mockito.times(1)).save(dayArgumentCaptor.capture());
        AgendaDay day = dayArgumentCaptor.getValue();

        Assertions.assertEquals(start,day.getEndDay());
        Assertions.assertEquals(start,day.getStartDay());
        Assertions.assertFalse(day.isAvailable());
        Assertions.assertEquals(PeriodType.DAY,day.getPeriodType());
        Assertions.assertNull(day.getOccupiedHour());
        Assertions.assertNull(day.getReservations());

    }

    @Test
    void insertDayInactivityErrorOverlappingTest() {
        LocalDate start = LocalDate.now();
        Mockito.when(repository.getOverlapping(start,start)).thenReturn(1);

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class,()-> service.insertDayInactivity(start));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST,e.getStatusCode());
    }

    @Test
    void getDayFromPeriod() {
    }
}