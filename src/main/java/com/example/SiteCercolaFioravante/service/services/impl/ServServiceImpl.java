package com.example.SiteCercolaFioravante.service.services.impl;

import com.example.SiteCercolaFioravante.reservation.Reservation;
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
import org.springframework.web.multipart.MultipartFile;

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
import java.util.UUID;

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
    public boolean insertService(ServiceDtoCompleteUpload service, List<MultipartFile> imagesDto) {

        Service serviceDB = new Service();
        mapper.ServiceDtoCompleteUploadToService(service,serviceDB);
        HashSet<String> images = new HashSet<String>();
        boolean insert = (imagesDto != null && !imagesDto.isEmpty());

        try {

            if( insert ) {
               images = transferToFile(imagesDto);
            }

            serviceDB.setImages(images);
            repository.save(serviceDB);
            repository.flush();


        }
        catch ( DataAccessException databaseError ){

            try {

              if( insert ) deleteFile( images );

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
        HashSet<String>  images = new HashSet<String>();
        if(serviceDB.getImages()!=null) images.addAll(serviceDB.getImages());
        ServiceDtoComplete serviceDtoComplete = new ServiceDtoComplete(serviceDB.getServiceName(), images,serviceDB.getPrice(),serviceDB.getDescription());;
        return serviceDtoComplete;
    }


    @Transactional
    @Override
    public boolean updateService(ServiceDtoCompleteUpload service, List<MultipartFile> imagesToInsert ) {
        Service serviceDB = repository.getServiceDbByName(service.prevServiceName());
        mapper.ServiceDtoCompleteUploadToService(service,serviceDB);
        HashSet<String> images =new HashSet<String>( serviceDB.getImages());
        HashSet<String> imagesins = new HashSet<String>();

        boolean insert = (imagesToInsert != null && !images.isEmpty());
        boolean remove = (service.imagesDataRemove() != null && !service.imagesDataRemove().isEmpty());

        try {

            if( insert ) {
                imagesins = transferToFile(imagesToInsert);
                images.addAll(imagesins);
            }

            if( remove ){
                images.removeAll(service.imagesDataRemove());
            }


            serviceDB.setImages(images);
            repository.save(serviceDB);
            repository.flush();
            //

            if( remove ) deleteFile(service.imagesDataRemove());

        } catch ( DataAccessException databaseError ){

            try {

                if( insert ) deleteFile( imagesins );

            } catch ( IOException catchErrorException ) {

                throw new RuntimeException( catchErrorException );

            }

            throw  databaseError;

        } catch ( IOException fileException ) {

            throw new RuntimeException( fileException );

        }


        return true;

    }

    private HashSet<String> transferToFile(List<MultipartFile> imagesToSave) throws IOException {

        HashSet<String> images = new HashSet<String>();

        Path directory = Paths.get(pathImage);

        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }


        for( MultipartFile image : imagesToSave ) {


            String name = UUID.randomUUID().toString()+image.getOriginalFilename();
            Path destinationPath = Paths.get(pathImage, name);
            InputStream inputStream = image.getInputStream();
            Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            images.add(name);


        }

        return images;

    }

    private void deleteFile(HashSet<String> imagesToDelete) throws IOException {

        for( String image : imagesToDelete ) {

            File imageFile = new File(Paths.get(pathImage, image).toUri());

            if(imageFile.exists() && imageFile.isFile() ) imageFile.delete();

        }

    }

    //TO-DO have to implement the image insert, update and service elimination
}
