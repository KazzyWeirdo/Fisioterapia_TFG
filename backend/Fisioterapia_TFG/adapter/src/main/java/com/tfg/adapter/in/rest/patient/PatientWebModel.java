package com.tfg.adapter.in.rest.patient;

import java.time.LocalDate;

public record PatientWebModel(
        int id, String email, String dni, String gender, String name, String surname, String secondSurname, int phoneNumber, LocalDate dateOfBirth) {

    static PatientWebModel fromDomainModel(com.tfg.patient.Patient patient) {
        return new PatientWebModel(
                patient.getId().value(),
                patient.getEmail().value(),
                patient.getDni().value(),
                patient.getGender().toString(),
                patient.getName(),
                patient.getSurname(),
                patient.getSecondSurname(),
                patient.getPhoneNumber(),
                patient.getDateOfBirth()
        );
    }
}
