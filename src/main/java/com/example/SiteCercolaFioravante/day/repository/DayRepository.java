package com.example.SiteCercolaFioravante.day.repository;

import com.example.SiteCercolaFioravante.day.Day;
import com.example.SiteCercolaFioravante.day.data_transfer_object.CalendarDayDtoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DayRepository extends JpaRepository<Day, Long> {

    @Query("select d.id as id, d.start as start, d.end as end, d.period_type as periodType,d.is_available as isAvailable,d.occupied_hour as occupiedHour from Day d WHERE d.date BETWEEN :start AND :end")
    List<CalendarDayDtoList> getDaysByTime(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT COUNT(*) FROM Day AS day WHERE day.start <= :end AND day.end >= :start")
    int getOverlapping(@Param("start") LocalDate start);

    Day findDayByStart();
}
