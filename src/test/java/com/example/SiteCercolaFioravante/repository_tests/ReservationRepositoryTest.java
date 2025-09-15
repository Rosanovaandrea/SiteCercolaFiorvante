package com.example.SiteCercolaFioravante.repository_tests;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.CustomerRole;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.example.SiteCercolaFioravante.day.Day;
import com.example.SiteCercolaFioravante.day.repository.DayRepository;
import com.example.SiteCercolaFioravante.reservation.Reservation;
import com.example.SiteCercolaFioravante.reservation.data_transfer_object.ReservationDto;
import com.example.SiteCercolaFioravante.reservation.repository.ReservationRepository;
import com.example.SiteCercolaFioravante.service.Service;
import com.example.SiteCercolaFioravante.service.repository.ServiceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@DataJpaTest
public class ReservationRepositoryTest {

    private Service service;
    private Day day;
    private Customer testCustomer;
    private Reservation reservation;

    private final ServiceRepository serviceRepository;
    private final CustomerRepository customerRepository;
    private final DayRepository dayRepository;
    private final ReservationRepository reservationRepository;

    public ReservationRepositoryTest(
                                    @Autowired ServiceRepository serviceRepository,
                                    @Autowired CustomerRepository customerRepository,
                                    @Autowired DayRepository dayRepository,
                                    @Autowired ReservationRepository reservationRepository
                                    ){

        this.customerRepository=customerRepository;
        this.dayRepository = dayRepository;
        this.serviceRepository = serviceRepository;
        this.reservationRepository = reservationRepository;

    }

    @BeforeEach
    void init(){
        service = new Service();
        service.setServiceName("massaggio");
        service.setPrice(200.0d);
        HashSet<String> images = new HashSet<String>();
        images.add("/first");
        service.setDescription("massaggio antani");

        testCustomer = new Customer();
        testCustomer.setName("andres");
        testCustomer.setSurname("rosanova");
        testCustomer.setRole(CustomerRole.ADMIN);
        testCustomer.setPhoneNumber("3333333333");

        day = new Day();
        day.setDate(LocalDate.now());
        HashSet<Integer> occupiedHour = new HashSet<Integer>();
        occupiedHour.add(1);
        day.setOccupiedHour(occupiedHour);
        day.setAvailable(true);

        reservation= new Reservation();
        reservation.setHour(1);
        reservation.setDay(day);
        reservation.setService(service);
        reservation.setCustomer(testCustomer);
        reservation.setCompleted(false);
        reservation.setDeletable(false);

        dayRepository.saveAndFlush(day);
        serviceRepository.saveAndFlush(service);
        customerRepository.saveAndFlush(testCustomer);

        day=dayRepository.getSingleDayDB(day.getDate());
        service= serviceRepository.getServiceDbByName(service.getServiceName());


        List<Reservation> reservations = new LinkedList<Reservation>();
        reservations.add(reservation);

        day.setReservations(reservations);
        service.setReservations(reservations);
        testCustomer.setReservations(reservations);



        reservationRepository.saveAndFlush(reservation);
        dayRepository.saveAndFlush(day);
        serviceRepository.saveAndFlush(service);
        customerRepository.saveAndFlush(testCustomer);

    }

    @Test
    void findReservationsByServiceNameTest(){
        Assertions.assertNotNull(reservationRepository.findReservationsByServiceName(service.getServiceName()));
        Assertions.assertNotNull(reservationRepository.findReservationsByServiceName(service.getServiceName()).get(0));
    }

    @Test
    void findReservationsByUserNameAndSurnameTest(){
      ReservationDto reservationDto = reservationRepository.findReservationsByUserNameAndSurname(testCustomer.getSurname()).get(0);
      Assertions.assertEquals(reservationDto.surname(),testCustomer.getSurname());
      Assertions.assertNotNull(dayRepository.getSingleDayDB(LocalDate.now()).getReservations());
    }

    @Test
    void findReservationsByLocalDateTest(){
       ReservationDto reservationDto = reservationRepository.findReservationsByLocalDate(day.getDate()).get(0);
        Assertions.assertEquals(reservationDto.date(),day.getDate());
    }

    @AfterEach
    void detatch(){
        reservationRepository.delete(reservation);
        serviceRepository.delete(service);
        dayRepository.delete(day);
        customerRepository.delete(testCustomer);
    }

}
