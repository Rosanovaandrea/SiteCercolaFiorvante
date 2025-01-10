package com.example.SiteCercolaFioravante.day;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface DayRepository extends JpaRepository<Day, Long> {

    Day getDayByDate(Date date);

    Day getDayById(long id);

    @Query("select d.date,res.hour from Day d JOIN FETCH d.reservations res WHERE d.date BETWEEN ':start' AND ':end")
    List<CalendarDayProjection> getDaysByTime(@Param("start") Date start,@Param("end") Date end);
}
