package com.example.SiteCercolaFioravante.service.services.impl;

import com.example.SiteCercolaFioravante.reservation.Reservation;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ImageDto;
import com.example.SiteCercolaFioravante.service.data_transfer_object.MapperService;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ServiceDtoComplete;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ServiceDtoCompleteUpload;
import com.example.SiteCercolaFioravante.service.repository.ServiceRepository;
import com.example.SiteCercolaFioravante.service.services.ServService;
import lombok.RequiredArgsConstructor;
import com.example.SiteCercolaFioravante.service.Service;
import org.springframework.dao.DataAccessException;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
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
    public boolean insertService(ServiceDtoCompleteUpload service) {

        Service serviceDB = new Service();

        mapper.ServiceDtoCompleteUploadToService(service,serviceDB);

        HashSet<String> images = serviceDB.getImages();

        try {

            transferToFile(service.imagesDataInsert(),"");

            // update images in database
            for (ImageDto imageDto : service.imagesDataInsert()) {

                if (imageDto.isFirstImage()) {
                    serviceDB.setFirstImage(imageDto.nameFile());//insert path
                }

                images.add(imageDto.nameFile()); // insert path
            }



            serviceDB.setImages(images);
            repository.save(serviceDB);
            repository.flush();
            //


        }
        catch ( DataAccessException databaseError ){

            try {
                    deleteFile( service.imagesDataInsert(),"" );

                } catch ( IOException catchErrorException ) {
                        throw new RuntimeException( catchErrorException );
                    }

            throw  new RuntimeException( databaseError );

        }catch (IOException e) {
            throw new RuntimeException(e);
        }



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
        Service serviceDB = repository.getServiceDtoCompleteByName(serviceName);
        ServiceDtoComplete serviceDtoComplete = new ServiceDtoComplete(serviceDB.getServiceName(),serviceDB.getImages(),serviceDB.getPrice(),serviceDB.getDescription());;
       return serviceDtoComplete;
    }

    @Override
    public boolean updateService(ServiceDtoCompleteUpload service) {
        Service serviceDB = repository.getServiceDbByName(service.prevServiceName());
        mapper.ServiceDtoCompleteUploadToService(service,serviceDB);
        HashSet<String> images = serviceDB.getImages();

        try {
            transferToFile(service.imagesDataInsert(),"");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            // update images in database
            for (ImageDto imageDto : service.imagesDataInsert()) {

                if (imageDto.isFirstImage()) {
                    serviceDB.setFirstImage(imageDto.nameFile());//insert path
                }

                images.add(imageDto.nameFile()); // insert path
            }

            for (ImageDto imageDto : service.imagesDataRemove()) {
                images.remove(imageDto.nameFile()); // insert path
            }


            serviceDB.setImages(images);
            repository.save(serviceDB);
            repository.flush();
            //


            deleteFile(service.imagesDataRemove(),"");

        }
        catch ( DataAccessException databaseError ){

            try {
                deleteFile( service.imagesDataInsert(),"" );

            } catch ( IOException catchErrorException ) {
                throw new RuntimeException( catchErrorException );
            }

            throw  new RuntimeException( databaseError );

        }

        catch ( IOException fileException ) {
            throw new RuntimeException( fileException );
        }


        return true;

    }

    private void transferToFile(List<ImageDto> imagesToSave, String path) throws IOException {

        for( ImageDto image : imagesToSave ) {
            File imageFile = new File(image.nameFile());
            image.file().transferTo(imageFile);
        }

    }

    private void deleteFile(List<ImageDto> imagesToSave, String path) throws IOException {

        for( ImageDto image : imagesToSave ) {
            File imageFile = new File(image.nameFile());
            imageFile.delete();
        }

    }

    //TO-DO have to implement the image insert, update and service elimination
}
