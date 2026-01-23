package com.tfg.port.in.patient;

import com.tfg.patient.*;

import java.util.Date;

public interface CreatePatientUseCase {
    void createPatient(String email,
                       String dni,
                       PatientGender gender,
                       String name,
                       String surname,
                       String secondSurname,
                       Date dateOfBirth,
                       int phoneNumber
                       );
}
