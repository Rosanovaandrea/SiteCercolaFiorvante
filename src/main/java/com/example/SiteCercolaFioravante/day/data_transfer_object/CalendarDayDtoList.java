package com.example.SiteCercolaFioravante.day.data_transfer_object;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record CalendarDayDtoList(
        @NotNull Date date,
        @NotNull boolean isAvailable
        ) {
}
