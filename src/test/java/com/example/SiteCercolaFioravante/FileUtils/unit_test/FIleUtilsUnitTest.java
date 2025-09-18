package com.example.SiteCercolaFioravante.FileUtils.unit_test;

import com.example.SiteCercolaFioravante.utils.impl.FIleUtilsImpl;
import com.example.SiteCercolaFioravante.utils.impl.FIleUtilsStaticWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

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
    void transferToFileTestInsertFileIOExceptionOnFirstFor() throws Exception {

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
    void transferToFileTestInsertFileExceptionOnRevertMethod() throws Exception {

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

        Assertions.assertThrows(RuntimeException.class,()->{fIleUtilsSample.transferToFile(imageNames, imagesToSave, pathImage);});

        Mockito.verify(fIleUtilsStaticWrapper).getPath(pathImage);
        Mockito.verify(fIleUtilsStaticWrapper, Mockito.times(2)).createDirectories(Mockito.any());
        Mockito.verify(fIleUtilsStaticWrapper, Mockito.times(1)).copyFile(Mockito.any(), Mockito.any());
        Mockito.verify(fIleUtilsSample,Mockito.times(1)).reverInsert(Mockito.any(),Mockito.any());
        Mockito.verify(fIleUtilsSample,Mockito.times(1)).cleanUpDirectory(Mockito.any());
    }

    @Test
    void transferToFileTestInsertFileIOExceptionOnSecondFor() throws Exception {

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

    @Test
    void FIleDeleteRightTest() throws IOException {
            String tmpPath =  "./tmp";

            FIleUtilsStaticWrapper fIleUtilsStaticWrapper = Mockito.mock();
            FIleUtilsImpl fIleUtils = Mockito.spy(new FIleUtilsImpl(tmpPath,fIleUtilsStaticWrapper));

            String pathImage ="./pathImage";

            HashSet<String> fileToRemove = new HashSet<>();
            fileToRemove.add("file1.jpg");
            fileToRemove.add("file2.jpg");

            Mockito.when(fIleUtilsStaticWrapper.getPath(pathImage)).thenReturn(Paths.get(pathImage));
            Mockito.doNothing().when(fIleUtilsStaticWrapper).createDirectories(Mockito.any());

            Path mockTmpPath = Mockito.mock(Path.class);
            UUID fixedUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
            Path tmpPathUUID = Paths.get(tmpPath).resolve(fixedUUID.toString());

            Mockito.when(mockTmpPath.resolve(fixedUUID.toString())).thenReturn(tmpPathUUID);
            Mockito.when(fIleUtilsStaticWrapper.getUUID()).thenReturn(fixedUUID);
            Mockito.when(fIleUtilsStaticWrapper.getPath(tmpPath)).thenReturn(mockTmpPath);
            Mockito.doNothing().when(fIleUtilsStaticWrapper).moveFile(Mockito.any(),Mockito.any());
            Mockito.doNothing().when(fIleUtils).cleanUpDirectory(tmpPathUUID);

            fIleUtils.deleteFiles(fileToRemove,pathImage);

            Mockito.verify(fIleUtilsStaticWrapper).getPath(pathImage);
            Mockito.verify(fIleUtilsStaticWrapper, Mockito.times(2)).createDirectories(Mockito.any());
            Mockito.verify(fIleUtilsStaticWrapper,Mockito.times(2)).moveFile(Mockito.any(),Mockito.any());
            Mockito.verify(fIleUtils,Mockito.times(1)).cleanUpDirectory(tmpPathUUID);
    }

    @Test
    void FIleDeleteRightTestThrowExceptionOutsideTry() throws IOException {
        String tmpPath =  "./tmp";

        FIleUtilsStaticWrapper fIleUtilsStaticWrapper = Mockito.mock();
        FIleUtilsImpl fIleUtils = Mockito.spy(new FIleUtilsImpl(tmpPath,fIleUtilsStaticWrapper));

        String pathImage ="./pathImage";

        HashSet<String> fileToRemove = new HashSet<>();
        fileToRemove.add("file1.jpg");
        fileToRemove.add("file2.jpg");

        Mockito.when(fIleUtilsStaticWrapper.getPath(pathImage)).thenReturn(Paths.get(pathImage));
        Mockito.doThrow(IOException.class).when(fIleUtilsStaticWrapper).createDirectories(Mockito.any());


        Assertions.assertThrows(IOException.class,()->{fIleUtils.deleteFiles(fileToRemove,pathImage);});

        Mockito.verify(fIleUtilsStaticWrapper).getPath(pathImage);
        Mockito.verify(fIleUtilsStaticWrapper, Mockito.times(1)).createDirectories(Mockito.any());
    }

    @Test
    void FIleDeleteTrhowExceptionInsideTry() throws Exception {
        String tmpPath =  "./tmp";

        FIleUtilsStaticWrapper fIleUtilsStaticWrapper = Mockito.mock();
        FIleUtilsImpl fIleUtils = Mockito.spy(new FIleUtilsImpl(tmpPath,fIleUtilsStaticWrapper));

        String pathImage ="./pathImage";

        HashSet<String> fileToRemove = new HashSet<>();
        fileToRemove.add("file1.jpg");
        fileToRemove.add("file2.jpg");
        Path imagePthPath = Paths.get(pathImage);

        Mockito.when(fIleUtilsStaticWrapper.getPath(pathImage)).thenReturn(imagePthPath);
        Mockito.doNothing().when(fIleUtilsStaticWrapper).createDirectories(Mockito.any());

        Path mockTmpPath = Mockito.mock(Path.class);
        UUID fixedUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        Path tmpPathUUID = Paths.get(tmpPath).resolve(fixedUUID.toString());

        Mockito.when(mockTmpPath.resolve(fixedUUID.toString())).thenReturn(tmpPathUUID);
        Mockito.when(fIleUtilsStaticWrapper.getUUID()).thenReturn(fixedUUID);
        Mockito.when(fIleUtilsStaticWrapper.getPath(tmpPath)).thenReturn(mockTmpPath);
        Mockito.doThrow(IOException.class).when(fIleUtilsStaticWrapper).moveFile(Mockito.any(),Mockito.any());
        Mockito.doNothing().when(fIleUtils).cleanUpDirectory(tmpPathUUID);

        Mockito.doNothing().when(fIleUtils).restoreBackup(Mockito.eq(tmpPathUUID),Mockito.eq(imagePthPath),Mockito.any());
        Assertions.assertThrows(IOException.class,()->{fIleUtils.deleteFiles(fileToRemove,pathImage);});

        ArgumentCaptor<LinkedList<String>> getlisttoBackup = ArgumentCaptor.forClass(LinkedList.class);


        Mockito.verify(fIleUtilsStaticWrapper,Mockito.times(1)).getPath(pathImage);
        Mockito.verify(fIleUtils, Mockito.times(1)).restoreBackup(Mockito.eq(tmpPathUUID),Mockito.eq(imagePthPath),getlisttoBackup.capture());

        LinkedList<String> listBackup=getlisttoBackup.getValue();
        Assertions.assertTrue(listBackup.size() == 1);
        Assertions.assertTrue(fileToRemove.contains(listBackup.get(0)));

        Mockito.verify(fIleUtilsStaticWrapper, Mockito.times(2)).createDirectories(Mockito.any());
        Mockito.verify(fIleUtilsStaticWrapper,Mockito.times(1)).moveFile(Mockito.any(),Mockito.any());
        Mockito.verify(fIleUtils,Mockito.times(1)).cleanUpDirectory(tmpPathUUID);
    }

    @Test
    void FIleDeleteTrhowExceptionRestoreBackup() throws Exception {
        String tmpPath =  "./tmp";

        FIleUtilsStaticWrapper fIleUtilsStaticWrapper = Mockito.mock();
        FIleUtilsImpl fIleUtils = Mockito.spy(new FIleUtilsImpl(tmpPath,fIleUtilsStaticWrapper));

        String pathImage ="./pathImage";

        HashSet<String> fileToRemove = new HashSet<>();
        fileToRemove.add("file1.jpg");
        fileToRemove.add("file2.jpg");
        Path imagePthPath = Paths.get(pathImage);

        Mockito.when(fIleUtilsStaticWrapper.getPath(pathImage)).thenReturn(imagePthPath);
        Mockito.doNothing().when(fIleUtilsStaticWrapper).createDirectories(Mockito.any());

        Path mockTmpPath = Mockito.mock(Path.class);
        UUID fixedUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        Path tmpPathUUID = Paths.get(tmpPath).resolve(fixedUUID.toString());

        Mockito.when(mockTmpPath.resolve(fixedUUID.toString())).thenReturn(tmpPathUUID);
        Mockito.when(fIleUtilsStaticWrapper.getUUID()).thenReturn(fixedUUID);
        Mockito.when(fIleUtilsStaticWrapper.getPath(tmpPath)).thenReturn(mockTmpPath);
        Mockito.doThrow(IOException.class).when(fIleUtilsStaticWrapper).moveFile(Mockito.any(),Mockito.any());
        Mockito.doNothing().when(fIleUtils).cleanUpDirectory(tmpPathUUID);

        Mockito.doThrow(Exception.class).when(fIleUtils).restoreBackup(Mockito.eq(tmpPathUUID),Mockito.eq(imagePthPath),Mockito.any());
        Assertions.assertThrows(RuntimeException.class,()->{fIleUtils.deleteFiles(fileToRemove,pathImage);});



        Mockito.verify(fIleUtilsStaticWrapper,Mockito.times(1)).getPath(pathImage);
        Mockito.verify(fIleUtils, Mockito.times(1)).restoreBackup(Mockito.eq(tmpPathUUID),Mockito.eq(imagePthPath),Mockito.any());

        Mockito.verify(fIleUtilsStaticWrapper, Mockito.times(2)).createDirectories(Mockito.any());
        Mockito.verify(fIleUtilsStaticWrapper,Mockito.times(1)).moveFile(Mockito.any(),Mockito.any());
        Mockito.verify(fIleUtils,Mockito.times(1)).cleanUpDirectory(tmpPathUUID);
    }

    @Test
    void restoreBackupTest() throws Exception {
        String tmpPath =  "./tmp";
        String pathImage ="./pathImage";
        UUID fixedUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        Path imagePthPath = Paths.get(pathImage);
        Path tmpPathUUID = Paths.get(tmpPath).resolve(fixedUUID.toString());
        FIleUtilsStaticWrapper fIleUtilsStaticWrapper = Mockito.mock();
        Mockito.doNothing().when(fIleUtilsStaticWrapper).moveFile(Mockito.any(),Mockito.any());


        FIleUtilsImpl fIleUtils = new FIleUtilsImpl(tmpPath,fIleUtilsStaticWrapper);

        LinkedList<String> fileToRestore = new LinkedList<>();

        fileToRestore.add("file.jpg");
        fileToRestore.add("file1.jpd");


        fIleUtils.restoreBackup(tmpPathUUID,imagePthPath,fileToRestore);

        Mockito.verify(fIleUtilsStaticWrapper,Mockito.times(1)).moveFile(Mockito.eq(tmpPathUUID.resolve(fileToRestore.get(0))),Mockito.eq(imagePthPath.resolve(fileToRestore.get(0))));
        Mockito.verify(fIleUtilsStaticWrapper,Mockito.times(1)).moveFile(Mockito.eq(tmpPathUUID.resolve(fileToRestore.get(1))),Mockito.eq(imagePthPath.resolve(fileToRestore.get(1))));

    }

    @Test
    void restoreBackupExceptionTest() throws Exception {
        String tmpPath =  "./tmp";
        String pathImage ="./pathImage";
        UUID fixedUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        Path imagePthPath = Paths.get(pathImage);
        Path tmpPathUUID = Paths.get(tmpPath).resolve(fixedUUID.toString());
        FIleUtilsStaticWrapper fIleUtilsStaticWrapper = Mockito.mock();
        Mockito.doThrow(IOException.class).when(fIleUtilsStaticWrapper).moveFile(Mockito.any(),Mockito.any());


        FIleUtilsImpl fIleUtils = new FIleUtilsImpl(tmpPath,fIleUtilsStaticWrapper);

        LinkedList<String> fileToRestore = new LinkedList<>();

        fileToRestore.add("file.jpg");
        fileToRestore.add("file1.jpd");


        Assertions.assertThrows(Exception.class,()->{fIleUtils.restoreBackup(tmpPathUUID,imagePthPath,fileToRestore);});

        Mockito.verify(fIleUtilsStaticWrapper,Mockito.times(1)).moveFile(Mockito.eq(tmpPathUUID.resolve(fileToRestore.get(0))),Mockito.eq(imagePthPath.resolve(fileToRestore.get(0))));

    }

    @Test
    void revertInsertRightTest() throws Exception {
            FIleUtilsStaticWrapper fIleUtilsStaticWrapper = Mockito.mock();
            String tmpString = "./tmp";
            String pathImage ="./image";
            LinkedHashSet<String> fileToRemove = new LinkedHashSet<>();

            Mockito.when(fIleUtilsStaticWrapper.existFile(Mockito.any())).thenReturn(true);
            Mockito.doNothing().when(fIleUtilsStaticWrapper).deleteFile(Mockito.any());

            fileToRemove.add("image.jpg");
            fileToRemove.add(("image1.jpg"));

            Path imagePath = Paths.get(pathImage);

            FIleUtilsImpl fIleUtils = new FIleUtilsImpl(tmpString,fIleUtilsStaticWrapper);

            fIleUtils.reverInsert(fileToRemove,imagePath);

            Mockito.verify(fIleUtilsStaticWrapper, Mockito.times(2)).existFile(Mockito.any());

            for(String image : fileToRemove){
                Mockito.verify(fIleUtilsStaticWrapper,Mockito.times(1)).deleteFile(Mockito.eq(imagePath.resolve(image)));
            }

    }

    @Test
    void revertInsertWithFalseExistTest() throws Exception {
        FIleUtilsStaticWrapper fIleUtilsStaticWrapper = Mockito.mock();
        String tmpString = "./tmp";
        String pathImage ="./image";
        LinkedHashSet<String> fileToRemove = new LinkedHashSet<>();

        Mockito.when(fIleUtilsStaticWrapper.existFile(Mockito.any())).thenReturn(true,false);
        Mockito.doNothing().when(fIleUtilsStaticWrapper).deleteFile(Mockito.any());

        fileToRemove.add("image.jpg");
        fileToRemove.add(("image1.jpg"));

        Path imagePath = Paths.get(pathImage);

        FIleUtilsImpl fIleUtils = new FIleUtilsImpl(tmpString,fIleUtilsStaticWrapper);

        fIleUtils.reverInsert(fileToRemove,imagePath);

        Mockito.verify(fIleUtilsStaticWrapper, Mockito.times(2)).existFile(Mockito.any());

        Mockito.verify(fIleUtilsStaticWrapper,Mockito.times(1)).deleteFile(Mockito.any());


    }  @Test
    void revertInsertExceptionTest() throws Exception {
        FIleUtilsStaticWrapper fIleUtilsStaticWrapper = Mockito.mock();
        String tmpString = "./tmp";
        String pathImage ="./image";
        LinkedHashSet<String> fileToRemove = new LinkedHashSet<>();

        Mockito.when(fIleUtilsStaticWrapper.existFile(Mockito.any())).thenReturn(true,false);
        Mockito.doThrow(IOException.class).when(fIleUtilsStaticWrapper).deleteFile(Mockito.any());

        fileToRemove.add("image.jpg");
        fileToRemove.add(("image1.jpg"));

        Path imagePath = Paths.get(pathImage);

        FIleUtilsImpl fIleUtils = new FIleUtilsImpl(tmpString,fIleUtilsStaticWrapper);

        Assertions.assertThrows(Exception.class,()->{fIleUtils.reverInsert(fileToRemove,imagePath);});

        Mockito.verify(fIleUtilsStaticWrapper, Mockito.times(1)).existFile(Mockito.any());

        Mockito.verify(fIleUtilsStaticWrapper,Mockito.times(1)).deleteFile(Mockito.any());


    }

    @Test
    void tryCleanUpTepDirectoryRightTest() throws IOException {

        FIleUtilsStaticWrapper fIleUtilsStaticWrapper = Mockito.mock();
        String tmpString = "./tmp";
        Path directory = Paths.get(tmpString);
        Path file1 = Paths.get("file1.jpg");
        Path file2 = Paths.get("file2.jpg");
        Path file3 = Paths.get("file3.jpg");

        Mockito.when(fIleUtilsStaticWrapper.existFile(Mockito.any())).thenReturn(true);
        Mockito.when(fIleUtilsStaticWrapper.walk(directory)).thenReturn(Stream.of(directory,file1,file2,file3));

        Mockito.doNothing().when(fIleUtilsStaticWrapper).deleteFile(file1);
        Mockito.doNothing().when(fIleUtilsStaticWrapper).deleteFile(file2);
        Mockito.doNothing().when(fIleUtilsStaticWrapper).deleteFile(file3);
        Mockito.doNothing().when(fIleUtilsStaticWrapper).deleteFile(directory);


        FIleUtilsImpl fIleUtils = new FIleUtilsImpl(tmpString,fIleUtilsStaticWrapper);

        fIleUtils.cleanUpDirectory(directory);

        InOrder inOrder = Mockito.inOrder(fIleUtilsStaticWrapper);

       inOrder.verify(fIleUtilsStaticWrapper).deleteFile(Mockito.eq(file3));
       inOrder.verify(fIleUtilsStaticWrapper).deleteFile(Mockito.eq(file2));
        inOrder.verify(fIleUtilsStaticWrapper).deleteFile(Mockito.eq(file1));
        inOrder.verify(fIleUtilsStaticWrapper).deleteFile(Mockito.eq(directory));

    }

    @Test
    void tryCleanUpTempDirectoryNotThrowOnErrorOnWalkInizialization() throws IOException {

        FIleUtilsStaticWrapper fIleUtilsStaticWrapper = Mockito.mock();
        String tmpString = "./tmp";
        Path directory = Paths.get(tmpString);

        Mockito.when(fIleUtilsStaticWrapper.existFile(directory)).thenReturn(true);

        Mockito.when(fIleUtilsStaticWrapper.walk(directory)).thenThrow(IOException.class);

        FIleUtilsImpl fIleUtils = new FIleUtilsImpl(tmpString,fIleUtilsStaticWrapper);

        fIleUtils.cleanUpDirectory(directory);

        Mockito.verify(fIleUtilsStaticWrapper,Mockito.times(0)).deleteFile(Mockito.any());
    }

    @Test
    void tryCleanUpTempDirectoryNotThrowOnErrorOnWalk() throws IOException {

        FIleUtilsStaticWrapper fIleUtilsStaticWrapper = Mockito.mock();
        String tmpString = "./tmp";
        Path directory = Paths.get(tmpString);
        Path file1 = Paths.get("file1.jpg");
        Path file2 = Paths.get("file2.jpg");
        Path file3 = Paths.get("file3.jpg");

        Mockito.when(fIleUtilsStaticWrapper.existFile(Mockito.any())).thenReturn(true);
        Mockito.when(fIleUtilsStaticWrapper.walk(directory)).thenReturn(Stream.of(directory,file1,file2,file3));

        FIleUtilsImpl fIleUtils = new FIleUtilsImpl(tmpString,fIleUtilsStaticWrapper);

        Mockito.doNothing().when(fIleUtilsStaticWrapper).deleteFile(file1);
        Mockito.doNothing().when(fIleUtilsStaticWrapper).deleteFile(file2);
        Mockito.doNothing().when(fIleUtilsStaticWrapper).deleteFile(file3);

        Mockito.doThrow(IOException.class).when(fIleUtilsStaticWrapper).deleteFile(directory);

        fIleUtils.cleanUpDirectory(directory);


        InOrder inOrder = Mockito.inOrder(fIleUtilsStaticWrapper);

        inOrder.verify(fIleUtilsStaticWrapper).deleteFile(Mockito.eq(file3));
        inOrder.verify(fIleUtilsStaticWrapper).deleteFile(Mockito.eq(file2));
        inOrder.verify(fIleUtilsStaticWrapper).deleteFile(Mockito.eq(file1));
        inOrder.verify(fIleUtilsStaticWrapper).deleteFile(Mockito.eq(directory));

    }


}

