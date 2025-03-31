package com.example.SiteCercolaFioravante.repository_tests;

import com.example.SiteCercolaFioravante.day.Day;
import com.example.SiteCercolaFioravante.day.data_transfer_object.CalendarDayDtoList;
import com.example.SiteCercolaFioravante.day.repository.DayRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@DataJpaTest
public class DayRepositoryTest {

    private Day day;
    private final DayRepository dayRepository;

    public DayRepositoryTest(@Autowired DayRepository repository){
        dayRepository = repository;
    }

    @BeforeEach
    public void init(){
        day = new Day();
        day.setDate(LocalDate.now());
        HashSet<Integer> occupiedHour = new HashSet<Integer>();
        occupiedHour.add(1);
        occupiedHour.add(2);
        day.setOccupiedHour(occupiedHour);
        day.setAvailable(true);
        dayRepository.save(day);
    }
    @Test
    void getDaysByTimeTest(){
       List<CalendarDayDtoList> days =dayRepository.getDaysByTime(day.getDate().minusMonths(1),day.getDate().plusMonths(1));
       CalendarDayDtoList day = days.get(0);
       Assertions.assertEquals(day.date(), this.day.getDate());
    }
    @Test
    void getSingleDayDBTest(){
        Assertions.assertEquals(day,dayRepository.getSingleDayDB(LocalDate.now()));
    }




    @AfterEach
    public void detach(){
        dayRepository.delete(day);
    }
}
