package com.example.SiteCercolaFioravante.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerDtoSafe(String surname,
                              @NotNull String name,
                              @Email String email,
                              @NotNull  String role,
                              @NotNull long phoneNumber) {
}
