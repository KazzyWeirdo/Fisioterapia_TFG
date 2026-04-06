package com.tfg.adapter.in.rest.patient;

import java.time.LocalDate;

public record PatientWebModel(
        int id, String email, String dni, String genderIdentity, String clinicalUseSex, String administrativeSex, String legalName, String nameToUse, String surname, String secondSurname, String pronouns, int phoneNumber, LocalDate dateOfBirth) {

    static PatientWebModel fromDomainModel(com.tfg.patient.Patient patient) {
        return new PatientWebModel(
                patient.getId().value(),
                patient.getEmail().value(),
                patient.getDni().value(),
                patient.getGenderIdentity().toString(),
                patient.getClinicalUseSex().toString(),
                patient.getAdministrativeSex().toString(),
                patient.getLegalName(),
                patient.getNameToUse(),
                patient.getSurname(),
                patient.getSecondSurname(),
                patient.getPronouns(),
                patient.getPhoneNumber(),
                patient.getDateOfBirth()
        );
    }
}
