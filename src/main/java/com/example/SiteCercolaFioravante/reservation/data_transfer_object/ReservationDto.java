package com.example.SiteCercolaFioravante.reservation.data_transfer_object;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public record ReservationDto(
        @NotNull long id,
        @NotNull LocalDate date,
        @NotNull String serviceName,
        @NotNull String name,
        @NotNull String surname,
        @NotNull int hour
) {
}
