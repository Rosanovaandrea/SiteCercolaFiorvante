package com.example.SiteCercolaFioravante.service.data_transfer_object;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;

public record ServiceDtoCompleteUpload(
        String prevServiceName,
        @NotNull String serviceName,
        @Size( max = 4 ) ArrayList< ImageDto > imagesDataInsert,
        @Size( max = 4 ) ArrayList< ImageDto > imagesDataRemove,
        @NotNull float price,
        @NotNull String description
) {
}
