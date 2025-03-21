package com.example.SiteCercolaFioravante.day.data_transfer_object;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record CalendarDtoSingleComplete(
       @NotNull Date date,
       @NotNull boolean isAvailable,
       @NotNull boolean[] occupiedHour

) {
}
