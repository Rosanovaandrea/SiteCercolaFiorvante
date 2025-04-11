package com.example.SiteCercolaFioravante.integration_service_tests;

import com.example.SiteCercolaFioravante.day.data_transfer_object.CalendarDayDtoList;
import com.example.SiteCercolaFioravante.day.repository.DayRepository;
import com.example.SiteCercolaFioravante.day.services.DayService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DayServiceTest {
    private final DayService dayService;
    private final DayRepository dayRepository;

    public DayServiceTest(
            @Autowired DayService dayService,
            @Autowired DayRepository dayRepository){
        this.dayService = dayService;
        this.dayRepository = dayRepository;
    }

    @Test
    @Order(1)
    void insertDayTest(){

        dayService.insertDayFalse(LocalDate.now());

        CalendarDayDtoList day1 = dayService.getDaysFromMonth(LocalDate.now()).get(0);

        Assertions.assertNotNull(day1);
        Assertions.assertEquals(LocalDate.now(),day1.date());


    }

    @Test
    @Order(2)
    void getDayFromMonthTest(){
        CalendarDayDtoList day = dayService.getDaysFromMonth(LocalDate.now()).get(0);
        Assertions.assertEquals(LocalDate.now(),day.date());

    }




}
