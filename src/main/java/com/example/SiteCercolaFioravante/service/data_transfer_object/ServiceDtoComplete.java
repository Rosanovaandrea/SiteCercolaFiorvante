package com.example.SiteCercolaFioravante.service.data_transfer_object;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;

public record ServiceDtoComplete(
                                    String prevServiceName,
                                    @NotNull String serviceName,
                                    ArrayList< String > images,
                                    @NotNull float price,
                                    @NotNull String description
                                    ) {}
