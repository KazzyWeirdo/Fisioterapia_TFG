package com.tfg.application.port.in.patient;

import com.tfg.model.patient.Patient;

public interface CreatePatientUseCase {
    void createPatient(Patient patient);
}
