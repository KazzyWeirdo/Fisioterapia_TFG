package com.tfg.application.port.in.patient;

import com.tfg.model.patient.PatientId;

public interface DeletePatientUseCase {
    void delete(PatientId patientId);
}
