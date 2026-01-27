package com.tfg.port.in.patient;

import com.tfg.patient.*;

import java.time.LocalDate;

public interface CreatePatientUseCase {
    void createPatient(String email,
                       String dni,
                       PatientGender gender,
                       String name,
                       String surname,
                       String secondSurname,
                       LocalDate dateOfBirth,
                       int phoneNumber
                       );
} // TODO: Consider using a DTO for better parameter management (but it can't have a dependency from adapter)
