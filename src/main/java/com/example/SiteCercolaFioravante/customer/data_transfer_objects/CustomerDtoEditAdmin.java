package com.example.SiteCercolaFioravante.customer.data_transfer_objects;

import com.example.SiteCercolaFioravante.customer.CustomerRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerDtoEditAdmin(
                                @NotNull @Email String prevEmail,
                                @NotNull String surname,
                                @NotNull String name,
                                @NotNull CustomerRole role,
                                @NotNull @Email String email,
                                @Size(max = 10, min=10) String phoneNumber) {
}
