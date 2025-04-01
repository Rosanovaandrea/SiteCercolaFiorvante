package com.example.SiteCercolaFioravante.reservation.repository;

import com.example.SiteCercolaFioravante.reservation.Reservation;
import com.example.SiteCercolaFioravante.reservation.data_transfer_object.ReservationDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT new com.example.SiteCercolaFioravante.reservation.data_transfer_object.ReservationDto" +
            "(day.date, service.serviceName, ctm.name, ctm.surname, e.hour) FROM Reservation e " +
            "JOIN e.service serv  " +
            "JOIN e.customer  ctm " +
            "JOIN e.day day " +
            "WHERE (ctm.name LIKE :nameSurname) OR (ctm.surname LIKE :nameSurname) ")
    List<ReservationDto> findReservationsByUserNameAndSurname( @Param("nameSurname") String nameSurname);

    @Query("SELECT new com.example.SiteCercolaFioravante.reservation.data_transfer_object.ReservationDto" +
            "(day.date, service.serviceName, ctm.name, ctm.surname, e.hour) FROM Reservation e " +
            "JOIN e.service serv  " +
            "JOIN e.customer  ctm " +
            "JOIN e.day day " +
            "WHERE ctm.email = :email ")
    List<ReservationDto> findReservationsByUserEmail(@Param( "email" ) String email );

    @Query("SELECT new com.example.SiteCercolaFioravante.reservation.data_transfer_object.ReservationDto" +
            "(day.date, service.serviceName, ctm.name, ctm.surname, e.hour) FROM Reservation e " +
            "JOIN e.service serv  " +
            "JOIN e.customer  ctm " +
            "JOIN e.day day " +
            "WHERE serv.serviceName = :serviceName ")
    List<ReservationDto> findReservationsByServiceName( @Param( "serviceName" ) String serviceName );

    @Query("SELECT new com.example.SiteCercolaFioravante.reservation.data_transfer_object.ReservationDto" +
            "(day.date, service.serviceName, ctm.name, ctm.surname, e.hour) FROM Reservation e " +
            "JOIN e.service serv  " +
            "JOIN e.customer  ctm " +
            "JOIN e.day day " +
            "WHERE day.date = :date ")
    List<ReservationDto> findReservationsByLocalDate( @Param( "date" ) LocalDate date );
}
