package com.example.SiteCercolaFioravante.day;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface DayRepository extends JpaRepository<Day, Long> {

    List<Day> getDayByDate(Date date);

    List<Day> getDayById(long id);

    @Query()
    List<Day> getDayByTime(Date start,Date end);
}
