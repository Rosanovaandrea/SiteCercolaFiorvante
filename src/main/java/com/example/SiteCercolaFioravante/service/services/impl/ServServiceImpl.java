package com.example.SiteCercolaFioravante.service.services.impl;

import com.example.SiteCercolaFioravante.reservation.Reservation;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ImageDto;
import com.example.SiteCercolaFioravante.service.data_transfer_object.MapperService;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ServiceDtoComplete;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ServiceDtoCompleteUpload;
import com.example.SiteCercolaFioravante.service.repository.ServiceRepository;
import com.example.SiteCercolaFioravante.service.services.ServService;
import com.example.SiteCercolaFioravante.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@org.springframework.stereotype.Service
public class ServServiceImpl implements ServService {


    private final String pathImage;
    private final ServiceRepository repository;
    private final MapperService mapper;

    public ServServiceImpl(
                           @Value("${images.path}") String pathImage,
                           @Autowired ServiceRepository repository,
                           @Autowired MapperService mapper
                    ) {
                        this.pathImage = pathImage;
                        this.repository = repository;
                        this.mapper = mapper;
                }


    @Override
    public void insertReservationInService(Reservation reservation) {
        Service serviceDB = reservation.getService();

        if(serviceDB.getReservations() == null)
            serviceDB.setReservations(new LinkedList<Reservation>());

        serviceDB.getReservations().add(reservation);

        repository.saveAndFlush(serviceDB);

    }

    @Override
    public Service getServiceForReservation(String serviceName) {
        return repository.getServiceDbByName(serviceName);
    }

    @Transactional
    @Override
    public boolean insertService(ServiceDtoCompleteUpload service) {

        Service serviceDB = new Service();

        mapper.ServiceDtoCompleteUploadToService(service,serviceDB);

        HashSet<String> images = new HashSet<String>();

        boolean insert = (service.imagesDataInsert() != null && !service.imagesDataInsert().isEmpty());

        try {

            if( insert ) {

                transferToFile(service.imagesDataInsert());

                for (ImageDto imageDto : service.imagesDataInsert()) {

                    if (imageDto.isFirstImage()) {
                        serviceDB.setFirstImage(Paths.get(pathImage,imageDto.nameFile()).toString());//insert path
                    }

                    images.add(Paths.get(pathImage,imageDto.nameFile()).toString()); // insert path
                }

            }

            serviceDB.setImages(images);
            repository.save(serviceDB);
            repository.flush();


        }
        catch ( DataAccessException databaseError ){

            try {

              if( insert ) deleteFile( service.imagesDataInsert() );

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            throw   databaseError;

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
        ServiceDtoComplete serviceDtoComplete = new ServiceDtoComplete(serviceDB.getServiceName(), new HashSet<String>( serviceDB.getImages()),serviceDB.getPrice(),serviceDB.getDescription());;
       return serviceDtoComplete;
    }

    @Transactional
    @Override
    public boolean updateService(ServiceDtoCompleteUpload service) {
        Service serviceDB = repository.getServiceDbByName(service.prevServiceName());
        mapper.ServiceDtoCompleteUploadToService(service,serviceDB);
        HashSet<String> images =new HashSet<String>( serviceDB.getImages());

        boolean insert = (service.imagesDataInsert() != null && !service.imagesDataInsert().isEmpty());
        boolean remove = (service.imagesDataRemove() != null && !service.imagesDataRemove().isEmpty());

        try {

            if( insert ) transferToFile(service.imagesDataInsert());

            } catch (IOException e) {

                throw new RuntimeException(e);

                }

        try {
            // update images in database
            if( insert ){

                for (ImageDto imageDto : service.imagesDataInsert()) {

                        if (imageDto.isFirstImage()) {
                                serviceDB.setFirstImage(imageDto.nameFile());//insert path
                            }

                        images.add(Paths.get(pathImage,imageDto.nameFile()).toString());
                    }
                }

            if( remove ){

                for (ImageDto imageDto : service.imagesDataRemove()) {
                             images.remove(Paths.get(pathImage,imageDto.nameFile()).toString());
                        }
            }


            serviceDB.setImages(images);
            repository.save(serviceDB);
            repository.flush();
            //

            if( remove ) deleteFile(service.imagesDataRemove());

        } catch ( DataAccessException databaseError ){

            try {

                if( insert ) deleteFile( service.imagesDataInsert() );

            } catch ( IOException catchErrorException ) {

                throw new RuntimeException( catchErrorException );

            }

            throw  databaseError;

        } catch ( IOException fileException ) {

            throw new RuntimeException( fileException );

        }


        return true;

    }

    private void transferToFile(List<ImageDto> imagesToSave) throws IOException {

        Path directory = Paths.get(pathImage);

        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }


        for( ImageDto image : imagesToSave ) {

            if (image.file() == null || image.file().isEmpty()) {
                throw new IllegalArgumentException();
            }

            Path destinationPath = Paths.get(pathImage, image.nameFile());
            InputStream inputStream = image.file().getInputStream();
            Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);

        }

    }

    private void deleteFile(List<ImageDto> imagesToDelete) throws IOException {

        for( ImageDto image : imagesToDelete ) {

            File imageFile = new File(Paths.get(pathImage, image.nameFile()).toUri());

            if(imageFile.exists() && imageFile.isFile() ) imageFile.delete();

        }

    }

    //TO-DO have to implement the image insert, update and service elimination
}
