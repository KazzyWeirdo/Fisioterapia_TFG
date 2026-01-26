package com.tfg.adapter.in.rest.patient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;


public record PatientCreationModel(
        @NotBlank(message = "Email is required")
        @Email
        String email,
        @NotBlank(message = "DNI is required")
        String dni,
        @NotBlank(message = "Gender is required")
        String gender,
        @NotBlank(message = "Name is required")
        String name,
        @NotBlank(message = "Surname is required")
        String surname,
        String secondSurname,
        int phoneNumber,
        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth
) {
}
