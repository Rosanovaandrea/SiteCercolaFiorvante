package com.example.SiteCercolaFioravante.integration_service_tests;

import com.example.SiteCercolaFioravante.day.data_transfer_object.CalendarDayDtoList;
import com.example.SiteCercolaFioravante.day.data_transfer_object.CalendarDtoSingleComplete;
import com.example.SiteCercolaFioravante.day.data_transfer_object.MapperDay;
import com.example.SiteCercolaFioravante.day.repository.DayRepository;
import com.example.SiteCercolaFioravante.day.services.DayService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

@RequiredArgsConstructor
public class DayServiceTest {
    private final DayService dayService;
    private final DayRepository dayRepository;
    private final MapperDay mapperDay;

    @Test
    @Order(1)
    void insertDayTest(){

        HashSet<Integer> occupiedHour = new HashSet<Integer>();
        occupiedHour.add(1);
        occupiedHour.add(2);
        CalendarDtoSingleComplete day = new CalendarDtoSingleComplete(LocalDate.now(),true,occupiedHour);
        dayService.insertDay(day);

        CalendarDayDtoList day1 = dayService.getDaysFromMonth(LocalDate.now()).get(0);

        Assertions.assertNotNull(day1);
        Assertions.assertEquals(day.date(),day1.date());

    }


}
