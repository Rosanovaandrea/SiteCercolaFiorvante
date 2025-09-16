package com.example.SiteCercolaFioravante.service.services;

import com.example.SiteCercolaFioravante.reservation.Reservation;
import com.example.SiteCercolaFioravante.service.Service;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ServiceDtoComplete;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ServiceDtoCompleteUpload;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ServService {

         void insertReservationInService(Reservation reservation);

         Service getServiceForReservation(String serviceName);

         boolean insertService(ServiceDtoCompleteUpload service, List<MultipartFile> images);

         List<String> getServicesNamesList();

         List<String> getServiceName(String serviceName);

         ServiceDtoComplete getServiceDtoCompleteByName( Long id);

         boolean updateService(ServiceDtoCompleteUpload service,List<MultipartFile> imageToInser);

}
