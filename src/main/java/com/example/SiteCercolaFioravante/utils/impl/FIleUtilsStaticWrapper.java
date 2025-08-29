package com.example.SiteCercolaFioravante.utils.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class FIleUtilsStaticWrapper {

    private final FileSystem fileSystem;

    public FIleUtilsStaticWrapper(@Autowired FileSystem fileSystem){
        this.fileSystem = fileSystem;
    }

    public UUID getUUID(){
        return UUID.randomUUID();
    }

    public Path getPath(String path){
       return fileSystem.getPath(path);
    }

    public void createDirectories(Path path) throws IOException {
        Files.createDirectories(path);
    }

    public void copyFile (InputStream inputStream, Path destinationPath) throws IOException {
        Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }

    public void moveFile (Path sourcePath, Path destinationPath) throws IOException {
        Files.move(sourcePath,destinationPath,StandardCopyOption.ATOMIC_MOVE);
    }

    public boolean existFile(Path path){
        return  Files.exists(path);
    }

    public void deleteFile(Path path) throws IOException {
        Files.delete(path);
    }
    public Stream<Path> walk(Path path) throws IOException {
        return Files.walk(path);
    }
}
