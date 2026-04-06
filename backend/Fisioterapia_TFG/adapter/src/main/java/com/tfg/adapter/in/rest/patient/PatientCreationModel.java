package com.tfg.adapter.in.rest.patient;

import com.tfg.patient.Patient;
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
        @NotBlank(message = "Gender identity is required")
        String genderIdentity,
        @NotBlank(message = "A sex for clinical use is required")
        String clinicalUseSex,
        @NotBlank(message = "Administrative sex is required")
        String administrativeSex,
        @NotBlank(message = "Legal Name is required")
        String legalName,
        @NotBlank(message = "Name for use is required")
        String nameToUse,
        @NotBlank(message = "Surname is required")
        String surname,
        String secondSurname,
        @NotBlank(message = "Pronouns are required")
        String pronouns,
        int phoneNumber,
        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth
) {
        public Patient toDomainModel(){
                return new Patient(email,
                        dni,
                        genderIdentity,
                        clinicalUseSex,
                        administrativeSex,
                        legalName,
                        nameToUse,
                        surname,
                        secondSurname,
                        pronouns,
                        dateOfBirth,
                        phoneNumber);
        }
}
