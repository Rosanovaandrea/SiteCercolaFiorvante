package com.example.SiteCercolaFioravante.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerDtoList(
        @NotNull String surname,
        @NotNull String name,
        @Email String email
) {

}
