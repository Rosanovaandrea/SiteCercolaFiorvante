package com.example.SiteCercolaFioravante.service.services.impl;

import com.example.SiteCercolaFioravante.reservation.Reservation;
import com.example.SiteCercolaFioravante.service.data_transfer_object.MapperService;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ServiceDtoComplete;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ServiceDtoCompleteUpload;
import com.example.SiteCercolaFioravante.service.repository.ServiceRepository;
import com.example.SiteCercolaFioravante.service.services.ServService;
import com.example.SiteCercolaFioravante.service.Service;
import com.example.SiteCercolaFioravante.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@org.springframework.stereotype.Service
@Slf4j
public class ServServiceImpl implements ServService {


    private final String pathImage;
    private final ServiceRepository repository;
    private final MapperService mapper;
    private final FileUtils fileUtils;

    public ServServiceImpl(
                           @Value("${images.path}") String pathImage,
                           @Autowired ServiceRepository repository,
                           @Autowired MapperService mapper,
                           @Autowired FileUtils fileUtils
                    ) {
                        this.pathImage = pathImage;
                        this.repository = repository;
                        this.mapper = mapper;
                        this.fileUtils = fileUtils;
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
        Service service = repository.getServiceDbByName(serviceName);
        if(service == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST," servizio non valido");
        return service;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean insertService(ServiceDtoCompleteUpload service, List<MultipartFile> imagesDto)  {

            LinkedHashSet<String> images= null;

            Service serviceDb = new Service();
            mapper.ServiceDtoCompleteUploadToService(service,serviceDb);

            if(imagesDto.isEmpty()){
                repository.save(serviceDb);
                return true;
            }

            if(imagesDto.size()>4)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"non puoi inserire più di 4 immagini");

            try{
                images = fileUtils.getImageNames(imagesDto);
                serviceDb.setImages(images);
                repository.save(serviceDb);

                fileUtils.transferToFile(images,imagesDto,pathImage);

                return true;

            } catch (Exception e) {
                log.error("c'è stato un errore nell'inserimento delle immagini del servizio"+e.getMessage());
                if(images != null) {
                    try {
                        fileUtils.reverInsert(images, Path.of(pathImage));
                    } catch (Exception ex) {
                        log.error("c'è stato un errore nella pulizia delle immagini in seguito ad un inserimento non riuscito"+e.getMessage());
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"errore grave nell'inserimento immagini",ex);
                    }
                }
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"errore nell'inserimento immagini",e);
            }
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
    public ServiceDtoComplete getServiceDtoCompleteByName(Long id) {
        Service serviceDB = repository.getReferenceById(id);
        HashSet<String>  images = new HashSet<String>();
        if(serviceDB.getImages()!=null) images.addAll(serviceDB.getImages());
        ServiceDtoComplete serviceDtoComplete = new ServiceDtoComplete(serviceDB.getId(), serviceDB.getServiceName(), images,serviceDB.getPrice(),serviceDB.getDescription());;
        return serviceDtoComplete;
    }


    @Transactional
    @Override
    public boolean updateService(ServiceDtoCompleteUpload service, List<MultipartFile> imagesToInsert ) {

        LinkedHashSet<String> imagesToInsertNames= null;

        Service serviceDb = repository.findById(service.id()).orElse(null);
        mapper.ServiceDtoCompleteUploadToService(service,serviceDb);


        LinkedHashSet<String> originalImages = (serviceDb.getImages() != null) ? (LinkedHashSet<String>) serviceDb.getImages(): new LinkedHashSet<>();



            if(imagesToInsert != null && !imagesToInsert.isEmpty()) {
                imagesToInsertNames = fileUtils.getImageNames(imagesToInsert);
            for (String image : imagesToInsertNames) {
                originalImages.add(image);
            }
        }


        HashSet<String> imageToRemove = service.imagesDataRemove();

        if(imageToRemove != null && !imageToRemove.isEmpty()){
        for(String image : imageToRemove ){
            originalImages.remove(image);
        }}

        serviceDb.setImages(originalImages);
        repository.save(serviceDb);


        try {
            if (imagesToInsertNames != null && !imagesToInsertNames.isEmpty()) {
                fileUtils.transferToFile(imagesToInsertNames, imagesToInsert, pathImage);
            }
            if (imageToRemove != null && !imageToRemove.isEmpty()) {
                fileUtils.deleteFiles(imageToRemove, pathImage);
            }
        }catch (Exception e) {
            log.error("c'è stato un errore nell'inserimento delle immagini del servizio"+e.getMessage());
            if(imagesToInsertNames != null && !imagesToInsertNames.isEmpty()) {
                try {
                    fileUtils.reverInsert(imagesToInsertNames, Path.of(pathImage));
                } catch (Exception ex) {
                    log.error("c'è stato un errore nella pulizia delle immagini in seguito ad un inserimento non riuscito"+e.getMessage());
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"errore grave nell'inserimento immagini",ex);
                }
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"errore nell'inserimento immagini",e);
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

    //TO-DO have to implement the service elimination
}
