package com.example.SiteCercolaFioravante.service.data_transfer_object;

import jakarta.validation.constraints.NotNull;

import java.util.HashSet;

public record ServiceDtoComplete(
                 Long id,
        @NotNull String serviceName,
        @NotNull HashSet<String> images,
        @NotNull double price,
        @NotNull String description) {}
