package com.example.SiteCercolaFioravante.utils.impl;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;


@Service
@Slf4j
public class FIleUtilsImpl {

    private final String tempPath;
    private final FIleUtilsStaticWrapper fileUtilsStaticWrapper;

    public FIleUtilsImpl(@Value("${temp.path}") @NotNull String tempPath ,
                         @Autowired FIleUtilsStaticWrapper fileUtilsStaticWrapper) {
        this.tempPath = tempPath;
        this.fileUtilsStaticWrapper = fileUtilsStaticWrapper;
    }

    public HashSet<String> getImageNames(@org.jetbrains.annotations.NotNull List<MultipartFile> imagesToSave){

        LinkedHashSet<String> images = new LinkedHashSet<>();
        for( int i = 0; i < imagesToSave.size(); i++ ) {

            String name = fileUtilsStaticWrapper.getUUID().toString();
            images.add(name);

        }
        return images;
    }

    public void transferToFile(LinkedHashSet<String> imageNames,List<MultipartFile> imagesToSave, String pathImage) throws IOException {

        if(imagesToSave.size() != imageNames.size()) throw new IllegalArgumentException("The number of filenames does not match the number of files. Data is out of sync.");

        Path destinationDirectoryPath = fileUtilsStaticWrapper.getPath(pathImage);

        fileUtilsStaticWrapper.createDirectories(destinationDirectoryPath);

        Path tempDirectoryPath = fileUtilsStaticWrapper.getPath(tempPath).resolve(fileUtilsStaticWrapper.getUUID().toString());

        fileUtilsStaticWrapper.createDirectories(tempDirectoryPath);

        ListIterator<MultipartFile> dataImage = imagesToSave.listIterator();

        LinkedHashSet<String> filesTORemove = new LinkedHashSet<>();

        try{
        for( String imageName : imageNames ) {
            Path destinationPath = tempDirectoryPath.resolve(imageName);
            try (InputStream inputStream = dataImage.next().getInputStream()) {
                fileUtilsStaticWrapper.copyFile(inputStream, destinationPath);
            }

        }

        for( String imageName : imageNames ) {
            filesTORemove.add(imageName);
            Path sourcePath = tempDirectoryPath.resolve(imageName);
            Path destinationPath = destinationDirectoryPath.resolve(imageName);
            fileUtilsStaticWrapper.moveFile(sourcePath,destinationPath);
        }
        }catch (Exception e){
            reverInsert(filesTORemove, destinationDirectoryPath);
            throw e;

        }finally {
            cleanUpDirectory(tempDirectoryPath);
        }

    }

    public void reverInsert(LinkedHashSet<String> deleterFiles,Path pathImage) throws IOException {
        try {
            for (String fileName : deleterFiles) {
                Path filePath = pathImage.resolve(fileName);
                if (fileUtilsStaticWrapper.existFile(filePath)) fileUtilsStaticWrapper.deleteFile(filePath);
            }
        }catch (Exception e){
            //log fata error, this rollback must not fail
            log.error("errore fatale durante il rollback dell'inserimento");
            throw new IOException("Rollback dell'inserimento fallito.", e);
        }
    }

    public void deleteFiles (LinkedList<String> fileTORemove,String pathImage) throws IOException {

        Path destinationDirectoryPath = fileUtilsStaticWrapper.getPath(pathImage);

        fileUtilsStaticWrapper.createDirectories(destinationDirectoryPath);

        Path tempDirectoryPath = fileUtilsStaticWrapper.getPath(tempPath).resolve(fileUtilsStaticWrapper.getUUID().toString());

        fileUtilsStaticWrapper.createDirectories(tempDirectoryPath);

        LinkedList<String> fileToBackup = new LinkedList<>();

        try{
        for( String imageName : fileTORemove ) {
            fileToBackup.add(imageName);
            Path backupPath = tempDirectoryPath.resolve(imageName);
            Path destinationToRemove = destinationDirectoryPath.resolve(imageName);
            fileUtilsStaticWrapper.moveFile(destinationToRemove,backupPath);
        }
        }catch(Exception e){
            restoreBackup(tempDirectoryPath,destinationDirectoryPath,fileToBackup);
            throw e;
        }finally {

            cleanUpDirectory(tempDirectoryPath);
        }



    }

    public void cleanUpDirectory(Path directory){
        if (Files.exists(directory)) {
            try (Stream<Path> pathStream = Files.walk(directory)) {
                pathStream.sorted(Comparator.reverseOrder()).forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException ex) {
                        log.warn("errore durante la cancellazione di un file temporaneo");
                    }
                });
            } catch (IOException e) {
                log.error("errore fatale durante la pulizia dei file temporanei");
            }
        }
    }

    private void restoreBackup(Path directoryBackup ,Path directoryRestore,LinkedList<String> fileToRestore) throws IOException {
       try{
        for( String imageName : fileToRestore ) {
            Path backupPath = directoryBackup.resolve(imageName);
            Path destinationToRemove = directoryRestore.resolve(imageName);
            fileUtilsStaticWrapper.moveFile(destinationToRemove,backupPath);
        }
       }catch (Exception e){
           log.error("errore fatale durante il rollback della cancellazione");
           throw new IOException("Rollback dell'inserimento fallito.", e);
       }
    }

}
