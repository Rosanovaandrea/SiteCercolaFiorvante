package com.example.SiteCercolaFioravante.ServService.unit_test;

import com.example.SiteCercolaFioravante.service.Service;
import com.example.SiteCercolaFioravante.service.data_transfer_object.MapperService;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ServiceDtoCompleteUpload;
import com.example.SiteCercolaFioravante.service.repository.ServiceRepository;
import com.example.SiteCercolaFioravante.service.services.impl.ServServiceImpl;
import com.example.SiteCercolaFioravante.utils.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServServiceUnitTest {

    @Mock
    private ServiceRepository repository;
    @Mock
    private MapperService mapper;
    @Mock
    private FileUtils fileUtils;

    private ServServiceImpl service;

    private ServiceDtoCompleteUpload serviceDto;

    private final String pathImage = "/path/to/images";

    @BeforeEach
    void setUp() {

        service = new ServServiceImpl(pathImage,repository,mapper,fileUtils);

        serviceDto = mock();
    }


    @Test
    void updateServiceRightTest() throws IOException {
        Service serviceDb = new Service();
        LinkedHashSet<String> imageNames = new LinkedHashSet<>(List.of("image1.jpg"));
        LinkedHashSet<String> originalimages = new LinkedHashSet<>(List.of("image.jpg","imageExists.jpg"));
        HashSet<String> fileToRemove = new HashSet<>(List.of("imageExists.jpg"));



        serviceDb.setImages(originalimages);

        List<MultipartFile> imagesDto = List.of(
                new MockMultipartFile("file1", "image1.jpg", "image/jpeg", "content".getBytes())
        );

        doNothing().when(mapper).ServiceDtoCompleteUploadToService(serviceDto,serviceDb);
        when(fileUtils.getImageNames(imagesDto)).thenReturn(imageNames);
        when(repository.findById(Mockito.any())).thenReturn(Optional.of(serviceDb));
        when(serviceDto.imagesDataRemove()).thenReturn(fileToRemove);



        boolean result = service.updateService(serviceDto,imagesDto);

        assertTrue(result);
        verify(fileUtils, Mockito.times(1)).deleteFiles(fileToRemove,pathImage);
        verify(fileUtils, Mockito.times(1)).transferToFile(imageNames,imagesDto,pathImage);
        verify(mapper, times(1)).ServiceDtoCompleteUploadToService(serviceDto, serviceDb);
        verify(fileUtils, times(1)).getImageNames(imagesDto);
        verify(repository, times(1)).save(serviceDb);

    }

    @Test
    void updateServiceRightNoOriginalImagesTest() throws IOException {
        Service serviceDb = new Service();
        LinkedHashSet<String> imageNames = new LinkedHashSet<>(List.of("image1.jpg"));
        HashSet<String> fileToRemove = new HashSet<>(List.of("imageExists.jpg"));

        List<MultipartFile> imagesDto = List.of(
                new MockMultipartFile("file1", "image1.jpg", "image/jpeg", "content".getBytes())
        );

        doNothing().when(mapper).ServiceDtoCompleteUploadToService(serviceDto,serviceDb);
        when(fileUtils.getImageNames(imagesDto)).thenReturn(imageNames);
        when(repository.findById(Mockito.any())).thenReturn(Optional.of(serviceDb));
        when(serviceDto.imagesDataRemove()).thenReturn(fileToRemove);



        boolean result = service.updateService(serviceDto,imagesDto);

        assertTrue(result);
        verify(fileUtils, Mockito.times(1)).deleteFiles(fileToRemove,pathImage);
        verify(fileUtils, Mockito.times(1)).transferToFile(imageNames,imagesDto,pathImage);
        verify(mapper, times(1)).ServiceDtoCompleteUploadToService(serviceDto, serviceDb);
        verify(fileUtils, times(1)).getImageNames(imagesDto);
        verify(repository, times(1)).save(serviceDb);

    }

    @Test
    void updateServiceRightNoDeleteImagesTest() throws IOException {
        Service serviceDb = new Service();
        LinkedHashSet<String> imageNames = new LinkedHashSet<>(List.of("image1.jpg"));

        List<MultipartFile> imagesDto = List.of(
                new MockMultipartFile("file1", "image1.jpg", "image/jpeg", "content".getBytes())
        );

        doNothing().when(mapper).ServiceDtoCompleteUploadToService(serviceDto,serviceDb);
        when(fileUtils.getImageNames(imagesDto)).thenReturn(imageNames);
        when(repository.findById(Mockito.any())).thenReturn(Optional.of(serviceDb));
        when(serviceDto.imagesDataRemove()).thenReturn(null);



        boolean result = service.updateService(serviceDto,imagesDto);

        assertTrue(result);
        verify(fileUtils, Mockito.times(0)).deleteFiles(null,pathImage);
        verify(fileUtils, Mockito.times(1)).transferToFile(imageNames,imagesDto,pathImage);
        verify(mapper, times(1)).ServiceDtoCompleteUploadToService(serviceDto, serviceDb);
        verify(fileUtils, times(1)).getImageNames(imagesDto);
        verify(repository, times(1)).save(serviceDb);

    }

    @Test
    void updateServiceRightNoInsertImagesTest() throws IOException {
        Service serviceDb = new Service();
        LinkedHashSet<String> imageNames = new LinkedHashSet<>(List.of("image1.jpg"));
        HashSet<String> fileToRemove = new HashSet<>(List.of("imageExists.jpg"));

        serviceDb.setImages(imageNames);

        doNothing().when(mapper).ServiceDtoCompleteUploadToService(serviceDto,serviceDb);
        when(repository.findById(Mockito.any())).thenReturn(Optional.of(serviceDb));
        when(serviceDto.imagesDataRemove()).thenReturn(fileToRemove);



        boolean result = service.updateService(serviceDto,null);

        assertTrue(result);
        verify(fileUtils, Mockito.times(1)).deleteFiles(fileToRemove,pathImage);
        verify(fileUtils, Mockito.times(0)).transferToFile(any(),any(),any());
        verify(mapper, times(1)).ServiceDtoCompleteUploadToService(serviceDto, serviceDb);
        verify(fileUtils, times(0)).getImageNames(Mockito.any());
        verify(repository, times(1)).save(serviceDb);

    }

    @Test
    void updateServiceErrorOnInsertTest() throws Exception {
        Service serviceDb = new Service();
        LinkedHashSet<String> imageNames = new LinkedHashSet<>(List.of("image1.jpg"));
        LinkedHashSet<String> originalimages = new LinkedHashSet<>(List.of("image.jpg","imageExists.jpg"));
        HashSet<String> fileToRemove = new HashSet<>(List.of("imageExists.jpg"));



        serviceDb.setImages(originalimages);

        List<MultipartFile> imagesDto = List.of(
                new MockMultipartFile("file1", "image1.jpg", "image/jpeg", "content".getBytes())
        );

        doNothing().when(mapper).ServiceDtoCompleteUploadToService(serviceDto,serviceDb);
        when(fileUtils.getImageNames(imagesDto)).thenReturn(imageNames);
        when(repository.findById(Mockito.any())).thenReturn(Optional.of(serviceDb));
        when(serviceDto.imagesDataRemove()).thenReturn(fileToRemove);
        doThrow(IOException.class).when(fileUtils).transferToFile(any(),any(),any());

        ResponseStatusException e = assertThrows(ResponseStatusException.class,()->service.updateService(serviceDto,imagesDto));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,e.getStatusCode());

        verify(fileUtils, Mockito.times(0)).deleteFiles(fileToRemove,pathImage);
        verify(fileUtils, Mockito.times(1)).transferToFile(imageNames,imagesDto,pathImage);
        verify(mapper, times(1)).ServiceDtoCompleteUploadToService(serviceDto, serviceDb);
        verify(fileUtils, times(1)).getImageNames(imagesDto);
        verify(fileUtils,times(1)).reverInsert(imageNames, Path.of(pathImage));
        verify(repository, times(1)).save(serviceDb);

    }

    @Test
    void updateServiceErrorOnRevertInsertTest() throws Exception {
        Service serviceDb = new Service();
        LinkedHashSet<String> imageNames = new LinkedHashSet<>(List.of("image1.jpg"));
        LinkedHashSet<String> originalimages = new LinkedHashSet<>(List.of("image.jpg","imageExists.jpg"));
        HashSet<String> fileToRemove = new HashSet<>(List.of("imageExists.jpg"));



        serviceDb.setImages(originalimages);

        List<MultipartFile> imagesDto = List.of(
                new MockMultipartFile("file1", "image1.jpg", "image/jpeg", "content".getBytes())
        );

        doNothing().when(mapper).ServiceDtoCompleteUploadToService(serviceDto,serviceDb);
        when(fileUtils.getImageNames(imagesDto)).thenReturn(imageNames);
        when(repository.findById(Mockito.any())).thenReturn(Optional.of(serviceDb));
        when(serviceDto.imagesDataRemove()).thenReturn(fileToRemove);
        doNothing().when(fileUtils).transferToFile(any(),any(),any());
        doThrow(IOException.class).when(fileUtils).deleteFiles(any(),any());

        ResponseStatusException e = assertThrows(ResponseStatusException.class,()->service.updateService(serviceDto,imagesDto));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,e.getStatusCode());

        verify(fileUtils, Mockito.times(1)).deleteFiles(fileToRemove,pathImage);
        verify(fileUtils, Mockito.times(1)).transferToFile(imageNames,imagesDto,pathImage);
        verify(mapper, times(1)).ServiceDtoCompleteUploadToService(serviceDto, serviceDb);
        verify(fileUtils, times(1)).getImageNames(imagesDto);
        verify(fileUtils,times(1)).reverInsert(imageNames, Path.of(pathImage));
        verify(repository, times(1)).save(serviceDb);

    }

    @Test
    void updateServiceErrorOnDeleteWithoutInsertTest() throws Exception {
        Service serviceDb = new Service();
        LinkedHashSet<String> imageNames = new LinkedHashSet<>(List.of("image1.jpg"));
        LinkedHashSet<String> originalimages = new LinkedHashSet<>(List.of("image.jpg","imageExists.jpg"));
        HashSet<String> fileToRemove = new HashSet<>(List.of("imageExists.jpg"));



        serviceDb.setImages(originalimages);

        List<MultipartFile> imagesDto = List.of(
                new MockMultipartFile("file1", "image1.jpg", "image/jpeg", "content".getBytes())
        );

        doNothing().when(mapper).ServiceDtoCompleteUploadToService(serviceDto,serviceDb);
        when(repository.findById(Mockito.any())).thenReturn(Optional.of(serviceDb));
        when(serviceDto.imagesDataRemove()).thenReturn(fileToRemove);
        doThrow(IOException.class).when(fileUtils).deleteFiles(any(),any());

        ResponseStatusException e = assertThrows(ResponseStatusException.class,()->service.updateService(serviceDto,null));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,e.getStatusCode());

        verify(fileUtils, Mockito.times(1)).deleteFiles(fileToRemove,pathImage);
        verify(fileUtils, Mockito.times(0)).transferToFile(imageNames,imagesDto,pathImage);
        verify(mapper, times(1)).ServiceDtoCompleteUploadToService(serviceDto, serviceDb);
        verify(fileUtils, times(0)).getImageNames(imagesDto);
        verify(fileUtils,times(0)).reverInsert(imageNames, Path.of(pathImage));
        verify(repository, times(1)).save(serviceDb);

    }

    @Test
    void updateServiceErrorOnDeleteTest() throws Exception {
        Service serviceDb = new Service();
        LinkedHashSet<String> imageNames = new LinkedHashSet<>(List.of("image1.jpg"));
        LinkedHashSet<String> originalimages = new LinkedHashSet<>(List.of("image.jpg","imageExists.jpg"));
        HashSet<String> fileToRemove = new HashSet<>(List.of("imageExists.jpg"));



        serviceDb.setImages(originalimages);

        List<MultipartFile> imagesDto = List.of(
                new MockMultipartFile("file1", "image1.jpg", "image/jpeg", "content".getBytes())
        );

        doNothing().when(mapper).ServiceDtoCompleteUploadToService(serviceDto,serviceDb);
        when(fileUtils.getImageNames(imagesDto)).thenReturn(imageNames);
        when(repository.findById(Mockito.any())).thenReturn(Optional.of(serviceDb));
        when(serviceDto.imagesDataRemove()).thenReturn(fileToRemove);
        doNothing().when(fileUtils).transferToFile(any(),any(),any());
        doThrow(IOException.class).when(fileUtils).deleteFiles(any(),any());
        doThrow(Exception.class).when(fileUtils).reverInsert(any(),any());

        ResponseStatusException e = assertThrows(ResponseStatusException.class,()->service.updateService(serviceDto,imagesDto));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,e.getStatusCode());

        verify(fileUtils, Mockito.times(1)).deleteFiles(fileToRemove,pathImage);
        verify(fileUtils, Mockito.times(1)).transferToFile(imageNames,imagesDto,pathImage);
        verify(mapper, times(1)).ServiceDtoCompleteUploadToService(serviceDto, serviceDb);
        verify(fileUtils, times(1)).getImageNames(imagesDto);
        verify(fileUtils,times(1)).reverInsert(imageNames, Path.of(pathImage));
        verify(repository, times(1)).save(serviceDb);

    }

    @Test
    void updateServiceErrorOnRetriveServiceTest() throws Exception {
        Service serviceDb = new Service();
        LinkedHashSet<String> imageNames = new LinkedHashSet<>(List.of("image1.jpg"));
        LinkedHashSet<String> originalimages = new LinkedHashSet<>(List.of("image.jpg","imageExists.jpg"));
        HashSet<String> fileToRemove = new HashSet<>(List.of("imageExists.jpg"));



        serviceDb.setImages(originalimages);

        List<MultipartFile> imagesDto = List.of(
                new MockMultipartFile("file1", "image1.jpg", "image/jpeg", "content".getBytes())
        );

        when(repository.findById(Mockito.any())).thenReturn(Optional.empty());

        ResponseStatusException e = assertThrows(ResponseStatusException.class,()->service.updateService(serviceDto,imagesDto));

        assertEquals(HttpStatus.BAD_REQUEST,e.getStatusCode());

        verify(fileUtils, Mockito.times(0)).deleteFiles(fileToRemove,pathImage);
        verify(fileUtils, Mockito.times(0)).transferToFile(imageNames,imagesDto,pathImage);
        verify(mapper, times(0)).ServiceDtoCompleteUploadToService(serviceDto, serviceDb);
        verify(fileUtils, times(0)).getImageNames(imagesDto);
        verify(fileUtils,times(0)).reverInsert(imageNames, Path.of(pathImage));
        verify(repository, times(0)).save(serviceDb);

    }
}
