package com.example.SiteCercolaFioravante.reservation.service;

import com.example.SiteCercolaFioravante.reservation.data_transfer_object.ReservationDto;
import com.example.SiteCercolaFioravante.reservation.data_transfer_object.ReservationDtoInsert;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
        boolean insertReservation(ReservationDtoInsert reservation);
        List<ReservationDto> findReservationByUserNameOrUsername(String nameUsername);
        List<ReservationDto> findReservationByServiceName(String serviceName);
        List<ReservationDto> findReservationByDate(LocalDate date);
        boolean removeReservation(long id);
}
