package com.example.SiteCercolaFioravante.day.repository;

import com.example.SiteCercolaFioravante.day.AgendaDay;
import com.example.SiteCercolaFioravante.day.data_transfer_object.CalendarDayDtoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendaDayRepository extends JpaRepository<AgendaDay, Long> {

    @Query("SELECT d FROM AgendaDay d LEFT JOIN FETCH d.occupiedHour  WHERE (:end >= d.startDay AND :start <= d.endDay) ORDER BY d.startDay ASC")
    List<AgendaDay> getDaysByTime(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT COUNT(*) FROM AgendaDay AS d WHERE (:end >= d.startDay AND :start <= d.endDay)")
    int getOverlapping(@Param("start") LocalDate start, @Param("end") LocalDate end);

    AgendaDay findDayByStartDay(LocalDate startDay);
}
