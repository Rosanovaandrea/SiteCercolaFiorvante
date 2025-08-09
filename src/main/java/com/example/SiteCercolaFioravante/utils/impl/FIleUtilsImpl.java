package com.example.SiteCercolaFioravante.utils.impl;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Stream;


@Service
@Slf4j
public class FIleUtilsImpl {

    private final String tempPath;

    public FIleUtilsImpl(@Value("${temp.path}") @NotNull String tempPath) {
        this.tempPath = tempPath;
    }

    public HashSet<String> getImageNames(@org.jetbrains.annotations.NotNull List<MultipartFile> imagesToSave){

        LinkedHashSet<String> images = new LinkedHashSet<>();
        for( MultipartFile image : imagesToSave ) {

            String name = UUID.randomUUID()+image.getOriginalFilename();
            images.add(name);

        }
        return images;
    }

    public void transferToFile(LinkedHashSet<String> imageNames,List<MultipartFile> imagesToSave, String pathImage) throws IOException {

        if(imagesToSave.size() != imageNames.size()) throw new IllegalArgumentException("The number of filenames does not match the number of files. Data is out of sync.");

        Path destinationDirectoryPath = Paths.get(pathImage);

        Files.createDirectories(destinationDirectoryPath);

        Path tempDirectoryPath = Paths.get(tempPath).resolve(UUID.randomUUID().toString());

        Files.createDirectories(tempDirectoryPath);

        ListIterator<MultipartFile> dataImage = imagesToSave.listIterator();

        LinkedHashSet<String> filesTORemove = new LinkedHashSet<String>();

        try{
        for( String imageName : imageNames ) {
            Path destinationPath = tempDirectoryPath.resolve(imageName);
            try (InputStream inputStream = dataImage.next().getInputStream()) {
                Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            }

        }

        for( String imageName : imageNames ) {
            filesTORemove.add(imageName);
            Path sourcePath = tempDirectoryPath.resolve(imageName);
            Path destinationPath = destinationDirectoryPath.resolve(imageName);
            Files.move(sourcePath,destinationPath,StandardCopyOption.ATOMIC_MOVE);
        }
        }catch (Exception e){
            reverInsert(filesTORemove, destinationDirectoryPath);
            throw e;

        }finally {
            cleanUpDirectory(tempDirectoryPath);
        }

    }

    private void reverInsert(LinkedHashSet<String> deleterFiles,Path pathImage) throws IOException {
        try {
            for (String fileName : deleterFiles) {
                Path filePath = pathImage.resolve(fileName);
                if (Files.exists(filePath)) Files.delete(filePath);
            }
        }catch (Exception e){
            //log fata error, this rollback must not fail
            log.error("errore fatale durante il rollback dell'inserimento");
            throw new IOException("Rollback dell'inserimento fallito.", e);
        }
    }

    public void deleteFiles (LinkedHashSet<String>fileToRevert, LinkedList<String> fileTORemove,String pathImage) throws IOException {

        Path destinationDirectoryPath = Paths.get(pathImage);

        Files.createDirectories(destinationDirectoryPath);

        Path tempDirectoryPath = Paths.get(tempPath).resolve(UUID.randomUUID().toString());

        Files.createDirectories(tempDirectoryPath);

        LinkedList<String> fileToBackup = new LinkedList<String>();

        try{
        for( String imageName : fileTORemove ) {
            fileToBackup.add(imageName);
            Path backupPath = tempDirectoryPath.resolve(imageName);
            Path destinationToRemove = destinationDirectoryPath.resolve(imageName);
            Files.move(destinationToRemove,backupPath,StandardCopyOption.ATOMIC_MOVE);
        }
        }catch(Exception e){
            restoreBackup(fileToRevert,tempDirectoryPath,destinationDirectoryPath,fileToBackup);
            throw e;
        }finally {

            cleanUpDirectory(tempDirectoryPath);
        }



    }

    private void cleanUpDirectory(Path directory) throws IOException {
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

    private void restoreBackup(LinkedHashSet<String> fileToRemove,Path directoryBackup ,Path directoryRestore,LinkedList<String> fileToRestore) throws IOException {
       try{
        for( String imageName : fileToRestore ) {
            Path backupPath = directoryBackup.resolve(imageName);
            Path destinationToRemove = directoryRestore.resolve(imageName);
            Files.move(destinationToRemove,backupPath,StandardCopyOption.ATOMIC_MOVE);
        }
       }catch (Exception e){
           log.error("errore fatale durante il rollback della cancellazione");
           throw new IOException("Rollback dell'inserimento fallito.", e);
       }
    }

}
