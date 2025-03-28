package com.example.SiteCercolaFioravante.day.repository;

import com.example.SiteCercolaFioravante.day.data_transfer_object.CalendarDayDtoList;
import com.example.SiteCercolaFioravante.day.data_transfer_object.CalendarDtoSingleComplete;
import com.example.SiteCercolaFioravante.day.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DayRepository extends JpaRepository<Day, Long> {

    @Query("select new com.example.SiteCercolaFioravante.day.data_transfer_object.CalendarDayDtoList(d.date, d.isAvailable)" +
            " from Day d WHERE d.date BETWEEN :start AND :end")
    List<CalendarDayDtoList> getDaysByTime(@Param("start") Date start, @Param("end") Date end);

    @Query("select new com.example.SiteCercolaFioravante.day.data_transfer_object.CalendarDtoSingleComplete(d.date,d.isAvailable, d.occupiedHour)" +
            " from Day d WHERE d.date = date")
    CalendarDtoSingleComplete getSingleDay(@Param("date")Date date);

}
