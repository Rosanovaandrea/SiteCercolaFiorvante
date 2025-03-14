package com.example.SiteCercolaFioravante.customer.data_transfer_objects;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerDtoSafe(
                              @NotNull String surname,
                              @NotNull String name,
                              @Email String email,
                              @NotNull @Size(max = 10, min=10) String phoneNumber) {
}
