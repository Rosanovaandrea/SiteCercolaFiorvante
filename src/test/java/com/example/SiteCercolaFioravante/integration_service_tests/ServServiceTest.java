package com.example.SiteCercolaFioravante.integration_service_tests;

import com.example.SiteCercolaFioravante.service.data_transfer_object.ImageDto;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ServiceDtoComplete;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ServiceDtoCompleteUpload;
import com.example.SiteCercolaFioravante.service.repository.ServiceRepository;
import com.example.SiteCercolaFioravante.service.services.ServService;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

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

        List<MultipartFile> imageDtos = new LinkedList<MultipartFile>();
        imageDtos.add(mockFile);

        ServiceDtoCompleteUpload serviceDtoCompleteUpload = new ServiceDtoCompleteUpload("massaggio",
                "massaggio",
                100.0f,
                null,
                "imassaggio rinfrescante"

                );

        servService.insertService(serviceDtoCompleteUpload,imageDtos);
        ServiceDtoComplete serviceDtoComplete = servService.getServiceDtoCompleteByName(serviceDtoCompleteUpload.serviceName());
        Assertions.assertEquals(serviceDtoComplete.serviceName(),serviceDtoCompleteUpload.serviceName());
        Assertions.assertEquals(serviceDtoComplete.description(),serviceDtoCompleteUpload.description());
        Assertions.assertEquals(serviceDtoComplete.price(),serviceDtoCompleteUpload.price());

        System.out.println(serviceDtoComplete.images().toArray()[0].toString());

    }

    @Test
    @Order(1)
    void updateServiceTest(){


        ServiceDtoComplete serviceDtoComplete = servService.getServiceDtoCompleteByName("massaggio");


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



        List<MultipartFile> imageDtos = new LinkedList<MultipartFile>();
        HashSet<String> imageDtos1 = new HashSet<String>();
        imageDtos.add(mockFile);
        imageDtos1.add( serviceDtoComplete.images().toArray()[0].toString());

        ServiceDtoCompleteUpload serviceDtoCompleteUpload = new ServiceDtoCompleteUpload(
                "massaggio",
                "massaggio2",
                50.0f,
                imageDtos1,
                "messaggio 2 dedos");
        servService.updateService(serviceDtoCompleteUpload,imageDtos);
        serviceDtoComplete = servService.getServiceDtoCompleteByName(serviceDtoCompleteUpload.serviceName());
        Assertions.assertEquals(serviceDtoComplete.serviceName(),serviceDtoCompleteUpload.serviceName());
        Assertions.assertEquals(serviceDtoComplete.description(),serviceDtoCompleteUpload.description());
        Assertions.assertEquals(serviceDtoComplete.price(),serviceDtoCompleteUpload.price());

        System.out.println(serviceDtoComplete.images().toArray()[0].toString());

    }
}
