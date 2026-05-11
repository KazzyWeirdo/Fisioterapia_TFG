package com.tfg.application.port.in.patient;

import com.tfg.model.patient.PatientId;

public interface DischargePatientUseCase {
    void discharge(PatientId patientId);
}
