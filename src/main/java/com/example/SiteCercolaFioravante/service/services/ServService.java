package com.example.SiteCercolaFioravante.service.services;

import com.example.SiteCercolaFioravante.reservation.Reservation;
import com.example.SiteCercolaFioravante.service.Service;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ServiceDtoComplete;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ServiceDtoCompleteUpload;

import java.util.List;

public interface ServService {

         void insertReservationInService(Reservation reservation);

         Service getServiceForReservation(String serviceName);

         boolean insertService(ServiceDtoCompleteUpload service);

         List<String> getServicesNamesList();

         List<String> getServiceName(String serviceName);

         ServiceDtoComplete getServiceDtoCompleteByName( String serviceName);

         boolean updateService(ServiceDtoCompleteUpload service);

}
