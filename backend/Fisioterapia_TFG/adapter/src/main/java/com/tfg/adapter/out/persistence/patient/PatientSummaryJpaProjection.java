package com.tfg.adapter.out.persistence.patient;

public record PatientSummaryJpaProjection(
        int id,
        String name,
        String surname,
        String secondSurname
) {
}
