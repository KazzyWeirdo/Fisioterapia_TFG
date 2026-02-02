package com.tfg.adapter.in.rest.pni;

import jakarta.validation.constraints.NotNull;

public record PniReportCreationModel(
        @NotNull(message = "Patient email is required")
        String patientEmail,
        @NotNull(message = "Hours asleep are required")
        Double hoursAsleep,
        Double hrv,
        Integer stress,
        @NotNull(message = "NTRS is required")
        Integer ntrs
) {
        public com.tfg.pni.PniReport toDomainModel(com.tfg.patient.Patient patient){
                return new com.tfg.pni.PniReport(
                        patient,
                        hoursAsleep,
                        hrv,
                        stress,
                        ntrs
                );
        }
}
