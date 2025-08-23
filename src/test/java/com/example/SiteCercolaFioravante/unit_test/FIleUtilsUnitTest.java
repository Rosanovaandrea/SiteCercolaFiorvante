package com.example.SiteCercolaFioravante.unit_test;

import com.example.SiteCercolaFioravante.utils.impl.FIleUtilsImpl;
import com.example.SiteCercolaFioravante.utils.impl.FIleUtilsStaticWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class FIleUtilsUnitTest {


    @Test
    void getImagesNamesTest(){

        FIleUtilsStaticWrapper fIleUtilsStaticWrapper = Mockito.mock(FIleUtilsStaticWrapper.class);
        String tempPath ="./tempPath";

        FIleUtilsImpl fIleUtilsSample = new FIleUtilsImpl(tempPath, fIleUtilsStaticWrapper);

        int total = 2;
        List<MultipartFile> mocklist = Mockito.mock();
        Mockito.when(mocklist.size()).thenReturn(total);
        Mockito.when(fIleUtilsStaticWrapper.getUUID()).thenReturn(
                UUID.fromString("68307970-bb6a-44df-8566-8c51de0089ce"),
                UUID.fromString("c115bb2c-b752-4160-934e-d552f77c172c")
        );

        HashSet<String> imageNames = fIleUtilsSample.getImageNames(mocklist);

        Assertions.assertEquals(total,imageNames.size());

    }

    @Test
    void getImagesNamesTestEmpty(){

        FIleUtilsStaticWrapper fIleUtilsStaticWrapper = Mockito.mock(FIleUtilsStaticWrapper.class);
        String tempPath ="./tempPath";

        FIleUtilsImpl fIleUtilsSample = new FIleUtilsImpl(tempPath, fIleUtilsStaticWrapper);

        int total = 0;
        List<MultipartFile> mocklist = Mockito.mock();
        Mockito.when(mocklist.size()).thenReturn(total);

        HashSet<String> imageNames = fIleUtilsSample.getImageNames(mocklist);

        Assertions.assertEquals(total,imageNames.size());

    }

    @Test
    void transferToFileTestInsertFileRight() throws IOException {

        FIleUtilsStaticWrapper fIleUtilsStaticWrapper = Mockito.mock(FIleUtilsStaticWrapper.class);
        String tempPath ="./tempPath";

        FIleUtilsImpl fIleUtilsSample = Mockito.spy(new FIleUtilsImpl(tempPath, fIleUtilsStaticWrapper));
        // Mock degli input
        LinkedHashSet<String> imageNames = new LinkedHashSet<>();
        imageNames.add("uuid1.jpg");
        imageNames.add("uuid2.png");

        List<MultipartFile> imagesToSave = new LinkedList<>();
        MultipartFile mockFile1 = Mockito.mock(MultipartFile.class);
        MultipartFile mockFile2 = Mockito.mock(MultipartFile.class);
        imagesToSave.add(mockFile1);
        imagesToSave.add(mockFile2);

        String pathImage = "/path/to/images";
        UUID fixedUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        // Mock del comportamento di fIleUtilsStaticWrapper
        Path destinationDirectoryPath = Paths.get(pathImage);
        Path mockTempPath = Mockito.mock(Path.class);
        Path tempDirectoryPath = Paths.get(tempPath).resolve("temp-uuid");


        Mockito.when(fIleUtilsStaticWrapper.getPath(pathImage)).thenReturn(destinationDirectoryPath);
        Mockito.when(fIleUtilsStaticWrapper.getPath(tempPath)).thenReturn(mockTempPath);
        Mockito.when(fIleUtilsStaticWrapper.getUUID()).thenReturn(fixedUUID);
        Mockito.when(mockTempPath.resolve(fixedUUID.toString())).thenReturn(tempDirectoryPath);

        Mockito.doNothing().when(fIleUtilsStaticWrapper).createDirectories(Mockito.any());
        Mockito.doNothing().when(fIleUtilsStaticWrapper).copyFile(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(fIleUtilsStaticWrapper).moveFile(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(fIleUtilsSample).cleanUpDirectory(Mockito.any());
        // Chiamata al metodo da testare
        fIleUtilsSample.transferToFile(imageNames, imagesToSave, pathImage);

        // Verifiche per confermare che le chiamate iniziali sono avvenute correttamente
        Mockito.verify(fIleUtilsStaticWrapper).getPath(pathImage);
        Mockito.verify(fIleUtilsStaticWrapper, Mockito.times(2)).createDirectories(Mockito.any());
        Mockito.verify(fIleUtilsStaticWrapper, Mockito.times(2)).copyFile(Mockito.any(), Mockito.any());
        Mockito.verify(fIleUtilsStaticWrapper, Mockito.times(2)).moveFile(Mockito.any(), Mockito.any());
        Mockito.verify(fIleUtilsSample,Mockito.times(1)).cleanUpDirectory(Mockito.any());
    }

    @Test
    void transferToFileTestInsertFileDifferentDimensionsLists() throws IOException {

        FIleUtilsStaticWrapper fIleUtilsStaticWrapper = Mockito.mock(FIleUtilsStaticWrapper.class);
        String tempPath ="./tempPath";

        FIleUtilsImpl fIleUtilsSample = new FIleUtilsImpl(tempPath, fIleUtilsStaticWrapper);
        // Mock degli input
        LinkedHashSet<String> imageNames = new LinkedHashSet<>();
        imageNames.add("uuid1.jpg");
        imageNames.add("uuid2.png");

        List<MultipartFile> imagesToSave = new LinkedList<>();
        MultipartFile mockFile1 = Mockito.mock(MultipartFile.class);
        imagesToSave.add(mockFile1);

        String pathImage = "/path/to/images";

        IllegalArgumentException e =Assertions.assertThrows(IllegalArgumentException.class,()->{fIleUtilsSample.transferToFile(imageNames, imagesToSave, pathImage);});
        Assertions.assertEquals("The number of filenames does not match the number of files. Data is out of sync.",e.getMessage());
    }

    @Test
    void transferToFileTestInsertFileIOHTrowIOExceptionOutsideTry() throws IOException {

        FIleUtilsStaticWrapper fIleUtilsStaticWrapper = Mockito.mock(FIleUtilsStaticWrapper.class);
        String tempPath ="./tempPath";

        FIleUtilsImpl fIleUtilsSample = new FIleUtilsImpl(tempPath, fIleUtilsStaticWrapper);
        LinkedHashSet<String> imageNames = new LinkedHashSet<>();
        imageNames.add("uuid1.jpg");
        imageNames.add("uuid2.png");

        List<MultipartFile> imagesToSave = new LinkedList<>();
        MultipartFile mockFile1 = Mockito.mock(MultipartFile.class);
        MultipartFile mockFile2 = Mockito.mock(MultipartFile.class);
        imagesToSave.add(mockFile1);
        imagesToSave.add(mockFile2);

        String pathImage = "/path/to/images";

        Path destinationDirectoryPath = Paths.get(pathImage);


        Mockito.when(fIleUtilsStaticWrapper.getPath(pathImage)).thenReturn(destinationDirectoryPath);
        Mockito.doThrow(IOException.class).when(fIleUtilsStaticWrapper).createDirectories(Mockito.any());

        Assertions.assertThrows(IOException.class ,()->{fIleUtilsSample.transferToFile(imageNames, imagesToSave, pathImage);});

        Mockito.verify(fIleUtilsStaticWrapper,Mockito.times(1)).getPath(pathImage);
        Mockito.verify(fIleUtilsStaticWrapper, Mockito.times(1)).createDirectories(Mockito.any());
    }

    @Test
    void transferToFileTestInsertFileIOExceptionOnFirstFor() throws IOException {

        FIleUtilsStaticWrapper fIleUtilsStaticWrapper = Mockito.mock(FIleUtilsStaticWrapper.class);
        String tempPath ="./tempPath";

        FIleUtilsImpl fIleUtilsSample = Mockito.spy(new FIleUtilsImpl(tempPath, fIleUtilsStaticWrapper));
        // Mock degli input
        LinkedHashSet<String> imageNames = new LinkedHashSet<>();
        imageNames.add("uuid1.jpg");
        imageNames.add("uuid2.png");

        List<MultipartFile> imagesToSave = new LinkedList<>();
        MultipartFile mockFile1 = Mockito.mock(MultipartFile.class);
        MultipartFile mockFile2 = Mockito.mock(MultipartFile.class);
        imagesToSave.add(mockFile1);
        imagesToSave.add(mockFile2);

        String pathImage = "/path/to/images";
        UUID fixedUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        // Mock del comportamento di fIleUtilsStaticWrapper
        Path destinationDirectoryPath = Paths.get(pathImage);
        Path mockTempPath = Mockito.mock(Path.class);
        Path tempDirectoryPath = Paths.get(tempPath).resolve("temp-uuid");


        Mockito.when(fIleUtilsStaticWrapper.getPath(pathImage)).thenReturn(destinationDirectoryPath);
        Mockito.when(fIleUtilsStaticWrapper.getPath(tempPath)).thenReturn(mockTempPath);
        Mockito.when(fIleUtilsStaticWrapper.getUUID()).thenReturn(fixedUUID);
        Mockito.when(mockTempPath.resolve(fixedUUID.toString())).thenReturn(tempDirectoryPath);

        Mockito.doNothing().when(fIleUtilsStaticWrapper).createDirectories(Mockito.any());
        Mockito.doNothing().when(fIleUtilsSample).reverInsert(Mockito.any(),Mockito.any());
        Mockito.doNothing().when(fIleUtilsSample).cleanUpDirectory(Mockito.any());

        Mockito.doThrow(IOException.class).when(fIleUtilsStaticWrapper).copyFile(Mockito.any(), Mockito.any());

        Assertions.assertThrows(IOException.class,()->{fIleUtilsSample.transferToFile(imageNames, imagesToSave, pathImage);});

        Mockito.verify(fIleUtilsStaticWrapper).getPath(pathImage);
        Mockito.verify(fIleUtilsStaticWrapper, Mockito.times(2)).createDirectories(Mockito.any());
        Mockito.verify(fIleUtilsStaticWrapper, Mockito.times(1)).copyFile(Mockito.any(), Mockito.any());
        Mockito.verify(fIleUtilsSample,Mockito.times(1)).reverInsert(Mockito.any(),Mockito.any());
        Mockito.verify(fIleUtilsSample,Mockito.times(1)).cleanUpDirectory(Mockito.any());
    }

    @Test
    void transferToFileTestInsertFileExceptionOnRevertMethod() throws IOException {

        FIleUtilsStaticWrapper fIleUtilsStaticWrapper = Mockito.mock(FIleUtilsStaticWrapper.class);
        String tempPath ="./tempPath";

        FIleUtilsImpl fIleUtilsSample = Mockito.spy(new FIleUtilsImpl(tempPath, fIleUtilsStaticWrapper));
        // Mock degli input
        LinkedHashSet<String> imageNames = new LinkedHashSet<>();
        imageNames.add("uuid1.jpg");
        imageNames.add("uuid2.png");

        List<MultipartFile> imagesToSave = new LinkedList<>();
        MultipartFile mockFile1 = Mockito.mock(MultipartFile.class);
        MultipartFile mockFile2 = Mockito.mock(MultipartFile.class);
        imagesToSave.add(mockFile1);
        imagesToSave.add(mockFile2);

        String pathImage = "/path/to/images";
        UUID fixedUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        // Mock del comportamento di fIleUtilsStaticWrapper
        Path destinationDirectoryPath = Paths.get(pathImage);
        Path mockTempPath = Mockito.mock(Path.class);
        Path tempDirectoryPath = Paths.get(tempPath).resolve("temp-uuid");


        Mockito.when(fIleUtilsStaticWrapper.getPath(pathImage)).thenReturn(destinationDirectoryPath);
        Mockito.when(fIleUtilsStaticWrapper.getPath(tempPath)).thenReturn(mockTempPath);
        Mockito.when(fIleUtilsStaticWrapper.getUUID()).thenReturn(fixedUUID);
        Mockito.when(mockTempPath.resolve(fixedUUID.toString())).thenReturn(tempDirectoryPath);

        Mockito.doNothing().when(fIleUtilsStaticWrapper).createDirectories(Mockito.any());
        Mockito.doNothing().when(fIleUtilsSample).cleanUpDirectory(Mockito.any());

        Mockito.doThrow(IOException.class).when(fIleUtilsStaticWrapper).copyFile(Mockito.any(), Mockito.any());
        Mockito.doThrow(IOException.class).when(fIleUtilsSample).reverInsert(Mockito.any(),Mockito.any());

        Assertions.assertThrows(IOException.class,()->{fIleUtilsSample.transferToFile(imageNames, imagesToSave, pathImage);});

        Mockito.verify(fIleUtilsStaticWrapper).getPath(pathImage);
        Mockito.verify(fIleUtilsStaticWrapper, Mockito.times(2)).createDirectories(Mockito.any());
        Mockito.verify(fIleUtilsStaticWrapper, Mockito.times(1)).copyFile(Mockito.any(), Mockito.any());
        Mockito.verify(fIleUtilsSample,Mockito.times(1)).reverInsert(Mockito.any(),Mockito.any());
        Mockito.verify(fIleUtilsSample,Mockito.times(1)).cleanUpDirectory(Mockito.any());
    }

    @Test
    void transferToFileTestInsertFileIOExceptionOnSecondFor() throws IOException {

        FIleUtilsStaticWrapper fIleUtilsStaticWrapper = Mockito.mock(FIleUtilsStaticWrapper.class);
        String tempPath ="./tempPath";

        FIleUtilsImpl fIleUtilsSample = Mockito.spy(new FIleUtilsImpl(tempPath, fIleUtilsStaticWrapper));
        // Mock degli input
        LinkedHashSet<String> imageNames = new LinkedHashSet<>();
        imageNames.add("uuid1.jpg");
        imageNames.add("uuid2.png");

        List<MultipartFile> imagesToSave = new LinkedList<>();
        MultipartFile mockFile1 = Mockito.mock(MultipartFile.class);
        MultipartFile mockFile2 = Mockito.mock(MultipartFile.class);
        imagesToSave.add(mockFile1);
        imagesToSave.add(mockFile2);

        String pathImage = "/path/to/images";
        UUID fixedUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        // Mock del comportamento di fIleUtilsStaticWrapper
        Path destinationDirectoryPath = Paths.get(pathImage);
        Path mockTempPath = Mockito.mock(Path.class);
        Path tempDirectoryPath = Paths.get(tempPath).resolve("temp-uuid");


        Mockito.when(fIleUtilsStaticWrapper.getPath(pathImage)).thenReturn(destinationDirectoryPath);
        Mockito.when(fIleUtilsStaticWrapper.getPath(tempPath)).thenReturn(mockTempPath);
        Mockito.when(fIleUtilsStaticWrapper.getUUID()).thenReturn(fixedUUID);
        Mockito.when(mockTempPath.resolve(fixedUUID.toString())).thenReturn(tempDirectoryPath);

        Mockito.doNothing().when(fIleUtilsStaticWrapper).createDirectories(Mockito.any());
        Mockito.doNothing().when(fIleUtilsSample).reverInsert(Mockito.any(),Mockito.any());
        Mockito.doNothing().when(fIleUtilsSample).cleanUpDirectory(Mockito.any());
        Mockito.doNothing().when(fIleUtilsStaticWrapper).copyFile(Mockito.any(),Mockito.any());

        Mockito.doThrow(IOException.class).when(fIleUtilsStaticWrapper).moveFile(Mockito.any(), Mockito.any());

        Assertions.assertThrows(IOException.class,()->{fIleUtilsSample.transferToFile(imageNames, imagesToSave, pathImage);});

        Mockito.verify(fIleUtilsStaticWrapper).getPath(pathImage);
        Mockito.verify(fIleUtilsStaticWrapper, Mockito.times(2)).createDirectories(Mockito.any());
        Mockito.verify(fIleUtilsStaticWrapper, Mockito.times(2)).copyFile(Mockito.any(), Mockito.any());
        Mockito.verify(fIleUtilsStaticWrapper, Mockito.times(1)).moveFile(Mockito.any(), Mockito.any());
        Mockito.verify(fIleUtilsSample,Mockito.times(1)).reverInsert(Mockito.any(),Mockito.any());
        Mockito.verify(fIleUtilsSample,Mockito.times(1)).cleanUpDirectory(Mockito.any());
    }
}

