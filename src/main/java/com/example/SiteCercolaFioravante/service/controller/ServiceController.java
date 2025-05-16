package com.example.SiteCercolaFioravante.service.controller;

import com.example.SiteCercolaFioravante.service.data_transfer_object.ServiceDtoComplete;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ServiceDtoCompleteUpload;
import com.example.SiteCercolaFioravante.service.services.ServService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/service")
@RequiredArgsConstructor
public class ServiceController {

    private final ServService servService;

        @PostMapping(path = "/insertService", consumes = {"multipart/form-data"})
        public ResponseEntity<Boolean> insertService(
                @RequestParam(required = false) List<MultipartFile> images,
                @RequestParam String serviceString) throws Exception {

            ObjectMapper objectMapper = new ObjectMapper();
            ServiceDtoCompleteUpload service = objectMapper.readValue(serviceString,ServiceDtoCompleteUpload.class);
            servService.insertService(service,images);

            return new ResponseEntity<>(true, HttpStatus.OK);
        }

        @GetMapping("/getServices")
        public ResponseEntity<List<String>> getServices(){
            return new ResponseEntity<>(servService.getServicesNamesList(), HttpStatus.OK);
        }

        @GetMapping("/getSingleService")
        public ResponseEntity<ServiceDtoComplete> getSingleService(@RequestParam String name){
           return new ResponseEntity<>(servService.getServiceDtoCompleteByName(name), HttpStatus.OK);
        }

        @PostMapping(path="/updateService", consumes = {"multipart/form-data"})
        public ResponseEntity<Boolean> updateService(@RequestParam(required = false) List<MultipartFile> images,
                                                     @RequestParam(required = false) List<String> removedImages,
                                                     @RequestParam String serviceString
                                                        ) throws Exception{

            ObjectMapper objectMapper = new ObjectMapper();
            ServiceDtoCompleteUpload service = objectMapper.readValue(serviceString,ServiceDtoCompleteUpload.class);

            //patched to manage the removed images ina an external post value in multiform-data; required a refactor
            if(removedImages!= null && !removedImages.isEmpty()){
                service = new ServiceDtoCompleteUpload(service.prevServiceName(),service.serviceName(),service.price(), new HashSet<>(removedImages),service.description());
            }
            servService.updateService(service,images);
            return new ResponseEntity<>(true, HttpStatus.OK);

        }

}
