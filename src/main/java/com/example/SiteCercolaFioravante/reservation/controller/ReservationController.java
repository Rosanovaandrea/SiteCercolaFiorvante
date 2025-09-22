package com.example.SiteCercolaFioravante.reservation.controller;

import com.example.SiteCercolaFioravante.reservation.data_transfer_object.ReservationDto;
import com.example.SiteCercolaFioravante.reservation.data_transfer_object.ReservationDtoInsert;
import com.example.SiteCercolaFioravante.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
//@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

        @PostMapping(path = "/insertReservation")
        public ResponseEntity<Boolean> insertReservation(
                @Valid @RequestBody ReservationDtoInsert reservation) throws Exception {

            reservationService.insertReservation(reservation);

            return new ResponseEntity<>(true, HttpStatus.OK);
        }

        @GetMapping("/findByDate")
        public ResponseEntity<List<ReservationDto>> getReservation(@RequestParam LocalDate start){
            return new ResponseEntity<>(reservationService.findReservationByDate(start),HttpStatus.OK);
        }

        @GetMapping("/today")
        public ResponseEntity<List<ReservationDto>> getReservationToday(){
            LocalDate now = LocalDate.now();
            return new ResponseEntity<>(reservationService.findReservationByDate(now),HttpStatus.OK);
        }

        @GetMapping("/serviceRes")
        public ResponseEntity<List<ReservationDto>> getReservationService(@RequestParam String serviceName){
            return new ResponseEntity<>(reservationService.findReservationByServiceName(serviceName),HttpStatus.OK);
        }

        @GetMapping("/customerRes")
        public ResponseEntity<List<ReservationDto>> getCustomerReservation(@RequestParam Long id){
            return new ResponseEntity<>(reservationService.findReservationByUserEmail(id),HttpStatus.OK);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Boolean> deleteReservation(@PathVariable Long id){
            reservationService.removeReservation(id);
            return new ResponseEntity<>(true,HttpStatus.OK);
        }





}
