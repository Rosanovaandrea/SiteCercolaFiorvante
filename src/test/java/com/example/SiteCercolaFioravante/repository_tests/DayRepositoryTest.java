package com.example.SiteCercolaFioravante.repository_tests;

import com.example.SiteCercolaFioravante.day.Day;
import com.example.SiteCercolaFioravante.day.PeriodType;
import com.example.SiteCercolaFioravante.day.repository.DayRepository;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;


@DataJpaTest
public class DayRepositoryTest {

    public static DayRepository repository;

    @BeforeAll
    static void init(@Autowired DayRepository repos){
        repository = repos;

        Day day1 = new Day();
        day1.setStart(LocalDate.of(2025, 10, 1));
        day1.setEnd(LocalDate.of(2025, 10, 1));
        day1.setPeriodType(PeriodType.DAY);
        day1.setAvailable(true);
        day1.setOccupiedHour(new HashSet<>());

// Test Day 2: A day that's not available and has some occupied hours.
        Day day2 = new Day();
        day2.setStart(LocalDate.of(2025, 10, 20));
        day2.setEnd(LocalDate.of(2025, 10, 20));
        day2.setPeriodType(PeriodType.DAY);
        day2.setAvailable(false);
        day2.setOccupiedHour(new HashSet<>(Arrays.asList(1, 2, 3)));

// Test Day 3: A day that's available but with a few occupied hours.
        Day day3 = new Day();
        day3.setStart(LocalDate.of(2025, 10, 15));
        day3.setEnd(LocalDate.of(2025, 10, 15));
        day3.setPeriodType(PeriodType.DAY);
        day3.setAvailable(true);
        day3.setOccupiedHour(new HashSet<>(Arrays.asList(3, 4)));

// Test Day 4: A past, non-available day with multiple occupied hours.
        Day day4 = new Day();
        day4.setStart(LocalDate.of(2025, 8, 15));
        day4.setEnd(LocalDate.of(2025, 8, 15));
        day4.setPeriodType(PeriodType.DAY);
        day4.setAvailable(false);

// Test Day 5: A day representing a weekly period.
        Day day5 = new Day();
        day5.setStart(LocalDate.of(2025, 11, 1));
        day5.setEnd(LocalDate.of(2025, 11, 7));
        day5.setPeriodType(PeriodType.PERIOD);
        day5.setAvailable(false);

    }
}
