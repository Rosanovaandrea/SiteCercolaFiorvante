package com.example.SiteCercolaFioravante.customer;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.LinkedList;

public record CustomerDtoComplete(

               long id,
  @NotNull     String surname,
  @NotNull     String name,
  @NotNull     String password,
  @NotNull @Email String email,
  @NotNull     String role,
  @NotNull     String phoneNumber) {
}
