package com.example.SiteCercolaFioravante.repository_tests;

import com.example.SiteCercolaFioravante.day.AgendaDay;
import com.example.SiteCercolaFioravante.day.PeriodType;
import com.example.SiteCercolaFioravante.day.repository.AgendaDayRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


@DataJpaTest
public class AgendaDayRepositoryTest {

    private AgendaDayRepository repository;
    private  AgendaDay day1;
    private  AgendaDay day2;
    private  AgendaDay day3;
    private  AgendaDay day4;
    private  AgendaDay day5;

    @BeforeEach
    void init(@Autowired AgendaDayRepository repos){
        repository = repos;

        day1 = new AgendaDay();
        day2 = new AgendaDay();
        day3 = new AgendaDay();
        day4 = new AgendaDay();
        day5 = new AgendaDay();

        day1.setStartDay(LocalDate.of(2025, 10, 1));
        day1.setEndDay(LocalDate.of(2025, 10, 1));
        day1.setPeriodType(PeriodType.DAY);
        day1.setAvailable(true);

// Test Day 2: A day that's not available and has some occupied hours.
        day2.setStartDay(LocalDate.of(2025, 10, 20));
        day2.setEndDay(LocalDate.of(2025, 10, 20));
        day2.setPeriodType(PeriodType.DAY);
        day2.setAvailable(true);
        day2.setOccupiedHour(new HashSet<>(Arrays.asList(1, 2, 3)));

// Test Day 3: A day that's available but with a few occupied hours.
        day3.setStartDay(LocalDate.of(2025, 10, 15));
        day3.setEndDay(LocalDate.of(2025, 10, 15));
        day3.setPeriodType(PeriodType.DAY);
        day3.setAvailable(true);
        day3.setOccupiedHour(new HashSet<>(Arrays.asList(3, 4)));

// Test Day 4: A past, non-available day with multiple occupied hours.
        day4.setStartDay(LocalDate.of(2025, 8, 15));
        day4.setEndDay(LocalDate.of(2025, 8, 15));
        day4.setPeriodType(PeriodType.DAY);
        day4.setAvailable(false);

// Test Day 5: A day representing a weekly period.
        day5.setStartDay(LocalDate.of(2025, 11, 1));
        day5.setEndDay(LocalDate.of(2025, 11, 7));
        day5.setPeriodType(PeriodType.PERIOD);
        day5.setAvailable(false);

        repository.save(day1);
        repository.save(day2);
        repository.save(day3);
        repository.save(day4);
        repository.save(day5);

        repository.flush();
    }

    @Test
    void getDaysByPeriodArePresentsTest(){
        LocalDate start = LocalDate.of(2025,9,1);
        LocalDate end = LocalDate.of(2025,11,25);

        List<AgendaDay> day_presents=repository.getDaysByTime(start,end);
        Assertions.assertEquals(4, day_presents.size());
        Assertions.assertEquals(day1.getStartDay(),day_presents.get(0).getStartDay());
        Assertions.assertNull(day_presents.get(0).getOccupiedHour() );
        Assertions.assertEquals(day3.getStartDay(),day_presents.get(1).getStartDay());
        Assertions.assertNotNull(day_presents.get(1).getOccupiedHour() );
        Assertions.assertEquals(day2.getStartDay(),day_presents.get(2).getStartDay());
        Assertions.assertNotNull(day_presents.get(2).getOccupiedHour() );
        Assertions.assertEquals(day5.getStartDay(),day_presents.get(3).getStartDay());
        Assertions.assertNull(day_presents.get(3).getOccupiedHour() );
    }

    @Test
    void getDaysByPeriodEmptyTest(){
        LocalDate start = LocalDate.of(2024,9,1);
        LocalDate end = LocalDate.of(2024,11,3);

        List<AgendaDay> day_presents=repository.getDaysByTime(start,end);
        Assertions.assertTrue(day_presents.isEmpty());
    }

    @Test
    void getDaysByPeriodArePresentsWithEndOutsideRangeTest(){
        LocalDate start = LocalDate.of(2025,9,1);
        LocalDate end = LocalDate.of(2025,11,4);

        List<AgendaDay> day_presents=repository.getDaysByTime(start,end);
        Assertions.assertEquals(4, day_presents.size());
        Assertions.assertEquals(day1.getStartDay(),day_presents.get(0).getStartDay());
        Assertions.assertNull(day_presents.get(0).getOccupiedHour() );
        Assertions.assertEquals(day3.getStartDay(),day_presents.get(1).getStartDay());
        Assertions.assertNotNull(day_presents.get(1).getOccupiedHour() );
        Assertions.assertEquals(day2.getStartDay(),day_presents.get(2).getStartDay());
        Assertions.assertNotNull(day_presents.get(2).getOccupiedHour() );
        Assertions.assertEquals(day5.getStartDay(),day_presents.get(3).getStartDay());
        Assertions.assertNull(day_presents.get(3).getOccupiedHour() );
    }

    @Test
    void getDaysByPeriodArePresentsWithInnerRangeTest(){
        LocalDate start = LocalDate.of(2025,11,2);
        LocalDate end = LocalDate.of(2025,11,4);

        List<AgendaDay> day_presents=repository.getDaysByTime(start,end);
        Assertions.assertEquals(1, day_presents.size());
        Assertions.assertEquals(day5.getStartDay(),day_presents.get(0).getStartDay());

    }

    @Test
    void overlappingIsPresentTest(){
        LocalDate start = LocalDate.of(2025,11,2);
        LocalDate end = LocalDate.of(2025,11,4);

        int count = repository.getOverlapping(start, end);
        Assertions.assertNotEquals(0, count);
        Assertions.assertTrue(count > 0);
    }

    @Test
    void overlappingNotPresentTest(){
        LocalDate start = LocalDate.of(2024,9,1);
        LocalDate end = LocalDate.of(2024,11,3);

        int count = repository.getOverlapping(start, end);
        Assertions.assertEquals(0, count);
    }

    @Test
    void overlappingArePresentsWithInnerRangeTest(){
        LocalDate start = LocalDate.of(2025,11,2);
        LocalDate end = LocalDate.of(2025,11,4);

        int count = repository.getOverlapping(start, end);
        Assertions.assertNotEquals(0, count);
        Assertions.assertTrue(count > 0);
    }

    @Test
    void findDayByStartDayTest(){
        AgendaDay day = repository.findDayByStartDay(day1.getStartDay());
        Assertions.assertEquals(day1.getStartDay(),day.getStartDay());
    }

    @Test
    void notFindDayByStartDayTest(){
        LocalDate start = LocalDate.of(2024,9,1);
        AgendaDay day = repository.findDayByStartDay(start);
        Assertions.assertNull(day);
    }


    @AfterEach
    void cleanup(){
        repository.deleteAll();
    }
}
