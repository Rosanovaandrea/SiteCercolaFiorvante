package com.example.SiteCercolaFioravante.service.services.impl;

import com.example.SiteCercolaFioravante.reservation.Reservation;
import com.example.SiteCercolaFioravante.service.data_transfer_object.MapperService;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ServiceDtoComplete;
import com.example.SiteCercolaFioravante.service.repository.ServiceRepository;
import com.example.SiteCercolaFioravante.service.services.ServService;
import lombok.RequiredArgsConstructor;
import com.example.SiteCercolaFioravante.service.Service;

import java.util.LinkedList;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServServiceImpl implements ServService {

    private final ServiceRepository repository;
    private final MapperService mapper;

    @Override
    public Service insertServiceForReservation(String serviceName, Reservation reservation) {
        Service serviceDB = repository.getServiceDbByName(serviceName);

        if ( serviceDB.getReservations() != null ) {
                     serviceDB.getReservations().add(reservation);
            } else {
                     LinkedList<Reservation> reservations = new LinkedList<Reservation>();
                     reservations.add(reservation);
                     serviceDB.setReservations(reservations);
                }

        repository.save(serviceDB);
        repository.flush();

        return serviceDB;
    }

    @Override
    public boolean insertService(ServiceDtoComplete service) {
        Service serviceDB = new Service();
        mapper.ServiceDtoCompleteToService(service,serviceDB);
        repository.save(serviceDB);
        repository.flush();
        return true;
    }

    @Override
    public List<String> getServicesNamesList() {
        return repository.getServiceListByNames();
    }

    @Override
    public List<String> getServiceName(String serviceName) {
        return repository.getServiceByName(serviceName);
    }

    @Override
    public ServiceDtoComplete getServiceDtoCompleteByName(String serviceName) {
        return repository.getServiceDtoCompleteByName(serviceName);
    }

    @Override
    public boolean updateService(ServiceDtoComplete service) {
        Service serviceDB = repository.getServiceDbByName(service.prevServiceName());
        mapper.ServiceDtoCompleteToService(service,serviceDB);
        repository.save(serviceDB);
        repository.flush();
        return true;
    }

    //TO-DO have to implement the image insert, update and service elimination
}
