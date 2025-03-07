package com.example.SiteCercolaFioravante.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerDtoSafe(
                              @NotNull String surname,
                              @NotNull String name,
                              @Email String email,
                              @NotNull  String role,
                              @NotNull @Size(max = 10, min=10) String phoneNumber) {
}
