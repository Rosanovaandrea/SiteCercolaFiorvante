package com.example.SiteCercolaFioravante.service.data_transfer_object;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public record ServiceDtoCompleteUpload(
        String prevServiceName,
        @NotNull String serviceName,
        @NotNull float price,
        HashSet<String> imagesDataRemove,
        @NotNull String description
) {
}
