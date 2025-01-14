package com.example.SiteCercolaFioravante.day.repository;

import com.example.SiteCercolaFioravante.day.Day;
import com.example.SiteCercolaFioravante.day.repository.CalendarDayProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DayRepository extends JpaRepository<Day, Long> {

   // @Query("select d.date, d.occupiedHour from Day d WHERE d.date BETWEEN ':start' AND ':end'")
  //  List<CalendarDayProjection> getDaysByTime(@Param("start") Date start,@Param("end") Date end);

}
