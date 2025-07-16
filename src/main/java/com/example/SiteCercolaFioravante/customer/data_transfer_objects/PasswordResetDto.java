package com.example.SiteCercolaFioravante.customer.data_transfer_objects;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record PasswordResetDto(
        @NotNull String token,
        @NotNull String password
) {
}
