package com.example.SiteCercolaFioravante.reservation.service.impl;

import com.example.SiteCercolaFioravante.customer.services.CustomerService;
import com.example.SiteCercolaFioravante.day.services.DayService;
import com.example.SiteCercolaFioravante.reservation.Reservation;
import com.example.SiteCercolaFioravante.reservation.data_transfer_object.ReservationDto;
import com.example.SiteCercolaFioravante.reservation.data_transfer_object.ReservationDtoInsert;
import com.example.SiteCercolaFioravante.reservation.repository.ReservationRepository;
import com.example.SiteCercolaFioravante.reservation.service.ReservationService;
import com.example.SiteCercolaFioravante.service.services.ServService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ServService servService;
    private final CustomerService customerService;
    private final DayService dayService;
    private final ReservationRepository repository;

    @Transactional
    @Override
    public boolean insertReservation(ReservationDtoInsert reservation) {
        Reservation reservationDB = new Reservation();
        reservationDB.setService(servService.getServiceForReservation(reservation.serviceName()));
        reservationDB.setCustomer(customerService.getCustomerFromEmailReservation(reservation.email()));
        reservationDB.setCompleted(false);
        reservationDB.setDeletable(false);
        reservationDB.setHour(reservation.hour());
        reservationDB.setDay(dayService.insertReservationfromReservation(reservation.date()));

        repository.saveAndFlush(reservationDB);

        servService.insertReservationInService(reservationDB);
        customerService.inserReservationCustomer(reservationDB);
        dayService.insertReservationInDay(reservationDB);

        return true;

    }

    @Override
    public List<ReservationDto> findReservationByUserEmail(String email) {
        return repository.findReservationsByUserEmail(email);
    }

    @Override
    public List<ReservationDto> findReservationByServiceName(String serviceName) {
        return repository.findReservationsByServiceName(serviceName);
    }

    @Override
    public List<ReservationDto> findReservationByDate(LocalDate date) {
        return repository.findReservationsByLocalDate(date);
    }

    @Transactional
    @Override
    public boolean removeReservation(long id) {
        Reservation reservation = repository.findById(id).orElse(null);
        if(reservation == null) throw new IllegalArgumentException();
        dayService.deleteReservationFromDay(reservation.getDay().getDate(),reservation.getHour());
        repository.delete(reservation);
        repository.flush();
        return true;
    }
}
