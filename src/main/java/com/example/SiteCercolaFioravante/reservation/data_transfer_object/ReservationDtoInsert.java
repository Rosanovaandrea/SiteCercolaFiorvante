package com.example.SiteCercolaFioravante.reservation.data_transfer_object;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public record ReservationDtoInsert(
                                    @NotNull Long customerId,
                                    @NotNull String serviceName,
                                    @NotNull LocalDate date,
                                    @NotNull int hour
                                    ) {}
