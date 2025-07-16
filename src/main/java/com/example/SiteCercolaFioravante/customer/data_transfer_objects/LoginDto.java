package com.example.SiteCercolaFioravante.customer.data_transfer_objects;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginDto(
        @NotNull @Email String email,
        @NotNull String password
) {
}
