package com.example.SiteCercolaFioravante.service.data_transfer_object;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.multipart.MultipartFile;

public record ImageDto(
                        @DefaultValue( "false" ) boolean isFirstImage,
                        @NotNull String nameFile,
                        @NotNull MultipartFile file
                    ){}
