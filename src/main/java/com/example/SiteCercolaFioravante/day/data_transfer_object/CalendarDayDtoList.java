package com.example.SiteCercolaFioravante.day.data_transfer_object;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public record CalendarDayDtoList(
        @NotNull LocalDate date,
        @NotNull boolean isAvailable
        ) {
}
