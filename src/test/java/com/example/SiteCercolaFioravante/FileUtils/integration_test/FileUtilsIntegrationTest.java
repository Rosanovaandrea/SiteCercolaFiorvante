package com.example.SiteCercolaFioravante.FileUtils.integration_test;


import com.example.SiteCercolaFioravante.utils.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
public class FileUtilsIntegrationTest {

    private FileUtils fileUtils;
    private FileSystem fileSystem;
    private String imagePath;
    private Path objectFroImagePath;


    @BeforeEach
    void setup(@Autowired FileSystem fileSystem,@Autowired FileUtils fileUtils, @Value("./imagePath") String imagePath){
        this.fileUtils = fileUtils;
        this.fileSystem = fileSystem;
        this. imagePath = imagePath;
        this.objectFroImagePath = fileSystem.getPath(imagePath);
    }

    @Test
    void fileUtilsTransferToFileTest() throws IOException {

        List<MultipartFile> fileToInsert = new LinkedList<>();

        MockMultipartFile file1 = new MockMultipartFile("image1.jpg","image1.jpg","jpg","content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("image2.jpg","image2.jpg","jpg","content".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("image3.jpg","image3.jpg","jpg","content".getBytes());

        fileToInsert.add(file1);
        fileToInsert.add(file2);
        fileToInsert.add(file3);

        LinkedHashSet<String> fileNames = fileUtils.getImageNames(fileToInsert);

        fileUtils.transferToFile(fileNames,fileToInsert,imagePath);


        for(String image : fileNames) {
           Assertions.assertTrue( Files.exists(objectFroImagePath.resolve( image ) ) );
        }

    }

    @Test
    void fileUtilsDeleteFileTest() throws IOException {

        HashSet<String> fileToRemove = new HashSet<>();
        fileToRemove.add("image1.jpg");
        fileToRemove.add("image2.jpg");
        fileToRemove.add("image3.jpg");

        Files.createDirectory(objectFroImagePath);

        for(String image : fileToRemove){
            Path file = objectFroImagePath.resolve(image);
            Files.createFile(file);
        }

        for(String image : fileToRemove) {
            Assertions.assertTrue( Files.exists(objectFroImagePath.resolve( image ) ) );
        }

        fileUtils.deleteFiles(fileToRemove,imagePath);

        for(String image : fileToRemove) {
            Assertions.assertFalse( Files.exists(objectFroImagePath.resolve( image ) ) );
        }
    }

    @Test
    void fileUtilsRevertInsertTest() throws Exception {

        List<MultipartFile> fileToInsert = new LinkedList<>();

        MockMultipartFile file1 = new MockMultipartFile("image1.jpg","image1.jpg","jpg","content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("image2.jpg","image2.jpg","jpg","content".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("image3.jpg","image3.jpg","jpg","content".getBytes());

        fileToInsert.add(file1);
        fileToInsert.add(file2);
        fileToInsert.add(file3);

        LinkedHashSet<String> fileNames = fileUtils.getImageNames(fileToInsert);

        for(String image : fileNames) {
            Path file = objectFroImagePath.resolve(image);
            Files.createFile(file);
        }

        for(String image : fileNames) {
            Assertions.assertTrue( Files.exists(objectFroImagePath.resolve( image ) ) );
        }

        fileUtils.reverInsert(fileNames,objectFroImagePath);


        for(String image : fileNames) {
            Assertions.assertFalse( Files.exists(objectFroImagePath.resolve( image ) ) );
        }

    }


}
