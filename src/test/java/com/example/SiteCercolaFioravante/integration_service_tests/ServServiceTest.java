package com.example.SiteCercolaFioravante.integration_service_tests;

import com.example.SiteCercolaFioravante.service.data_transfer_object.ImageDto;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ServiceDtoComplete;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ServiceDtoCompleteUpload;
import com.example.SiteCercolaFioravante.service.repository.ServiceRepository;
import com.example.SiteCercolaFioravante.service.services.ServService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServServiceTest {
    private final ServService servService;
    private final ServiceRepository serviceRepository;

    public ServServiceTest(
            @Autowired ServService servService,
            @Autowired ServiceRepository serviceRepository) {
        this.servService = servService;
        this.serviceRepository = serviceRepository;
    }

    @Test
    @Order(1)
    void insertServiceTest(){

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
        ServiceDtoComplete serviceDtoComplete = servService.getServiceDtoCompleteByName(serviceDtoCompleteUpload.serviceName());
        Assertions.assertEquals(serviceDtoComplete.serviceName(),serviceDtoCompleteUpload.serviceName());
        Assertions.assertEquals(serviceDtoComplete.description(),serviceDtoCompleteUpload.description());
        Assertions.assertEquals(serviceDtoComplete.price(),serviceDtoCompleteUpload.price());

        System.out.println(serviceDtoComplete.images().toArray()[0].toString());

    }

    @Test
    @Order(1)
    void updateServiceTest(){

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

         name = "imageFile2"; // Nome del campo nel form
         originalFilename = "test_image2.jpg";
         contentType = "image/jpeg";
         content = "Questo è il contenuto di un'immagine finta".getBytes();

        // Crea l'oggetto MockMultipartFile
        MultipartFile mockFile2 = new MockMultipartFile(
                name,
                originalFilename,
                contentType,
                content
        );

        ImageDto image2 = new ImageDto(true, "imageFile2.jpg",mockFile2);

        ArrayList<ImageDto> imageDtos = new ArrayList<ImageDto>();
        ArrayList<ImageDto> imageDtos1 = new ArrayList<ImageDto>();
        imageDtos.add(image2);
        imageDtos1.add(image);

        ServiceDtoCompleteUpload serviceDtoCompleteUpload = new ServiceDtoCompleteUpload("massaggio",
                "massaggio2",
                imageDtos,
                imageDtos1,
                50.0f,
                "massaggio rilassante stocazzico");

        servService.updateService(serviceDtoCompleteUpload);
        ServiceDtoComplete serviceDtoComplete = servService.getServiceDtoCompleteByName(serviceDtoCompleteUpload.serviceName());
        Assertions.assertEquals(serviceDtoComplete.serviceName(),serviceDtoCompleteUpload.serviceName());
        Assertions.assertEquals(serviceDtoComplete.description(),serviceDtoCompleteUpload.description());
        Assertions.assertEquals(serviceDtoComplete.price(),serviceDtoCompleteUpload.price());

        System.out.println(serviceDtoComplete.images().toArray()[0].toString());

    }
}
