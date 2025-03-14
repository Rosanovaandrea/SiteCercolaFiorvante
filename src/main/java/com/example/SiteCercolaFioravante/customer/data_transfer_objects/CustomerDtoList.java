package com.example.SiteCercolaFioravante.customer.data_transfer_objects;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerDtoList(
        @NotNull String surname,
        @NotNull String name,
        @NotNull @Email String email
) {

}
