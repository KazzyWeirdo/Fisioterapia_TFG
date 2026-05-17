package com.tfg.application.exceptions;

import com.tfg.model.patient.PatientId;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(PatientId patientId) {
        super("Patient not found: " + patientId.value());
    }
}
