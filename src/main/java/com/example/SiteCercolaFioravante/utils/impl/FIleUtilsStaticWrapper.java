package com.example.SiteCercolaFioravante.utils.impl;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FIleUtilsStaticWrapper {
    public UUID getUUID(){
        return UUID.randomUUID();
    }

    public Path getPath(String path){
       return Paths.get(path);
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
