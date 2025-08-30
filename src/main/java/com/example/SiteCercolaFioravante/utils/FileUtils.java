package com.example.SiteCercolaFioravante.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public interface FileUtils {

    LinkedHashSet<String> getImageNames(List<MultipartFile> imagesToSave);

    void transferToFile(LinkedHashSet<String> imageNames, List<MultipartFile> imagesToSave, String pathImage) throws IOException;

    void reverInsert(LinkedHashSet<String> deleterFiles,Path pathImage) throws Exception;

    void deleteFiles (LinkedList<String> fileTORemove, String pathImage) throws IOException;

}
