package com.example.SiteCercolaFioravante.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerDtoEditAdmin(@NotNull String surname,
                                  @NotNull String name,
                                  @NotNull CustomerRole role,
                                  @Email String email,
                                  @NotNull @Size(max = 10, min=10) String phoneNumber) {
}
