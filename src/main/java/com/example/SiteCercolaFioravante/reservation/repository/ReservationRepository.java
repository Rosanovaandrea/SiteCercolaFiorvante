package com.example.SiteCercolaFioravante.reservation.repository;

import com.example.SiteCercolaFioravante.reservation.Reservation;
import com.example.SiteCercolaFioravante.reservation.repository.ReservationProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT day.date, service.serviceName, user.name, user.surname, e.hour, e.id  FROM Reservation e " +
            "JOIN e.service serv  " +
            "JOIN e.user  user " +
            "JOIN e.day day " +
            "WHERE user.email = :email ")
    List<ReservationProjection> findReservationsByUserNameAndSurname( @Param( "email" ) String email );

    @Query("SELECT day.date, service.serviceName, user.name, user.surname, e.hour, e.id  FROM Reservation e " +
            "JOIN e.service serv  " +
            "JOIN e.user  user " +
            "JOIN e.day day " +
            "WHERE serv.serviceName = :serviceName ")
    List<ReservationProjection> findReservationsByServiceName( @Param( "serviceName" ) String serviceName );

    @Query("SELECT day.date, service.serviceName, user.name, user.surname, e.hour, e.id  FROM Reservation e " +
            "JOIN e.service serv  " +
            "JOIN e.user  user " +
            "JOIN e.day day " +
            "WHERE day.date = :date ")
    List<ReservationProjection> findReservationsByDate( @Param( "date" ) Date date );
}
