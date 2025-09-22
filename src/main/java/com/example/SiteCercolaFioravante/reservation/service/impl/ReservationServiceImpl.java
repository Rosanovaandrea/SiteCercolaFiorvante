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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl {


}
