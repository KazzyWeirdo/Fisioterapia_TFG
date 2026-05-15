package com.tfg.adapter.out.persistence.patient;

import com.tfg.model.patient.Pathology;

public record PatientSummaryJpaProjection(
        int id,
        String name,
        String surname,
        String secondSurname,
        Pathology pathology,
        Integer functionalScore,
        java.time.LocalDate dischargeDate
) {
}
