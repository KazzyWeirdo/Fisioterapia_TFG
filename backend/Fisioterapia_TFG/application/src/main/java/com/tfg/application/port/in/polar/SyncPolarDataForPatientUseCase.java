package com.tfg.application.port.in.polar;

import com.tfg.model.patient.PatientId;

public interface SyncPolarDataForPatientUseCase {
    void syncForPatient(PatientId patientId);
}
