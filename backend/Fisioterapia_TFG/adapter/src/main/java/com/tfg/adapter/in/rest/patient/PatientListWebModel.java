package com.tfg.adapter.in.rest.patient;

public record PatientListWebModel(
        int id,
        String name,
        String surname,
        String secondSurname,
        String pathology,
        Integer functionalScore,
        java.time.LocalDate dischargeDate
) {
}
