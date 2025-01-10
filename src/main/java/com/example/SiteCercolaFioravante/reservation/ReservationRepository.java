package com.example.SiteCercolaFioravante.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT day.date, service.serviceName, user.name, user.surname, e.isCompleted, e.isDeletable  FROM Reservation e " +
            "JOIN e.service serv  " +
            "JOIN e.user  user " +
            "JOIN e.day day " +
            "WHERE user.name = :name OR user.surname = :surname ")
    List<ReservationProjection> findReservationsByUserNameAndSurname(@Param("name")String name, @Param("surname") String surname);

    @Query("SELECT day.date, service.serviceName, user.name, user.surname, e.isCompleted, e.isDeletable  FROM Reservation e " +
            "JOIN e.service serv  " +
            "JOIN e.user  user " +
            "JOIN e.day day " +
            "WHERE serv.serviceName = :name ")
    List<ReservationProjection> findReservationsByServiceName(@Param("name")String name);

    @Query("SELECT day.date, service.serviceName, user.name, user.surname, e.isCompleted, e.isDeletable  FROM Reservation e " +
            "JOIN e.service serv  " +
            "JOIN e.user  user " +
            "JOIN e.day day " +
            "WHERE day.date = :date ")
    List<ReservationProjection> findReservationsByDate(@Param("date") Date date);
}
