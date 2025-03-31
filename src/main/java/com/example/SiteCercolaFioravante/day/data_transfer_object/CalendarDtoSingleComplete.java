package com.example.SiteCercolaFioravante.day.data_transfer_object;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.HashSet;

public record CalendarDtoSingleComplete(
       @NotNull LocalDate date,
       @NotNull boolean isAvailable,
       @NotNull HashSet<Integer> occupiedHour

) {
}
