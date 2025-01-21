package com.example.SiteCercolaFioravante.customer;

public record CustomerDtoSafe(String surname,
                              String name,
                              String email,

                              String role,
                              long phoneNumber) {
}
