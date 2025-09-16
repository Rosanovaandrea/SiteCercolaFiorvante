package com.example.SiteCercolaFioravante.integration_service_tests;

import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.repository.CustomerRepository;
import com.example.SiteCercolaFioravante.customer.services.CustomerService;
import com.example.SiteCercolaFioravante.day.repository.DayRepository;
import com.example.SiteCercolaFioravante.day.services.DayService;
import com.example.SiteCercolaFioravante.reservation.data_transfer_object.ReservationDto;
import com.example.SiteCercolaFioravante.reservation.repository.ReservationRepository;
import com.example.SiteCercolaFioravante.reservation.service.ReservationService;
import com.example.SiteCercolaFioravante.service.repository.ServiceRepository;
import com.example.SiteCercolaFioravante.service.services.ServService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservationServiceTest {

    private final ReservationService reservationService;
    private final CustomerService customerService;
    private final DayService dayService;
    private  final ServService servService;
    private final CustomerRepository customerRepository;
    private final DayRepository dayRepository;
    private final ServiceRepository serviceRepository;
    private final ReservationRepository reservationRepository;


    public ReservationServiceTest(
            @Autowired ReservationService reservationService,
            @Autowired CustomerService customerService,
            @Autowired DayService dayService,
            @Autowired ServService servService,
            @Autowired CustomerRepository customerRepository,
            @Autowired DayRepository dayRepository,
            @Autowired ServiceRepository serviceRepository,
            @Autowired ReservationRepository reservationRepository
    ) {
        this.reservationService = reservationService;
        this.customerService = customerService;
        this.dayService = dayService;
        this.servService = servService;
        this.customerRepository = customerRepository;
        this.dayRepository = dayRepository;
        this.serviceRepository = serviceRepository;
        this.reservationRepository = reservationRepository;
    }


/*
    void insertReservationTest(){

        CustomerDtoSafe customer = new CustomerDtoSafe(
                "andrea","rossi","","1111114111"
        );

        customerService.insertCustomerFromAdmin(customer);

        String name = "imageFile"; // Nome del campo nel form
        String originalFilename = "test_image.jpg";
        String contentType = "image/jpeg";
        byte[] content = "Questo è il contenuto di un'immagine finta".getBytes();

        // Crea l'oggetto MockMultipartFile
        MultipartFile mockFile = new MockMultipartFile(
                name,
                originalFilename,
                contentType,
                content
        );

        ImageDto image = new ImageDto(true, "imageFile.jpg",mockFile);

        ArrayList<ImageDto> imageDtos = new ArrayList<ImageDto>();
        imageDtos.add(image);

        ServiceDtoCompleteUpload serviceDtoCompleteUpload = new ServiceDtoCompleteUpload("massaggio",
                "massaggio",
                imageDtos,
                null,
                100.0f,
                "massaggio rilassante");

        servService.insertService(serviceDtoCompleteUpload);
        String customerId = customerService.getCustomerByNameOrSurname(customer.name()).get(0).customerId();

        ReservationDtoInsert reservationDtoInsert = new ReservationDtoInsert(customerId,"massaggio", LocalDate.now(), 2);

        reservationService.insertReservation(reservationDtoInsert);

        ReservationDto reservationDto = reservationService.findReservationByDate(LocalDate.now()).get(0);

        Assertions.assertEquals(reservationDto.date(),reservationDtoInsert.date());
        Assertions.assertEquals(reservationDto.serviceName(),reservationDtoInsert.serviceName());

    }
*/
    @Test
    @Order(1)
    void deleteReservationTest(){
        ReservationDto reservationDto = reservationService.findReservationByDate(LocalDate.now()).get(0);
        reservationService.removeReservation(reservationDto.id());
    }
}
