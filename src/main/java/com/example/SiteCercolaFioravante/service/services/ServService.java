package com.example.SiteCercolaFioravante.service.services;

import com.example.SiteCercolaFioravante.reservation.Reservation;
import com.example.SiteCercolaFioravante.service.Service;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ServiceDtoComplete;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ServiceDtoCompleteUpload;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServService {

         Service insertServiceForReservation(String serviceName, Reservation reservation);

         boolean insertService(ServiceDtoCompleteUpload service);

         List<String> getServicesNamesList();

         List<String> getServiceName(String serviceName);

         ServiceDtoComplete getServiceDtoCompleteByName( String serviceName);

         boolean updateService(ServiceDtoCompleteUpload service);

}
