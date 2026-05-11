package com.tfg.application.pojos.query;

public record PatientSummaryElement(
        int id,
        String name,
        String surname,
        String secondSurname,
        String pathology,
        Integer functionalScore,
        java.time.LocalDate dischargeDate
) {
}
