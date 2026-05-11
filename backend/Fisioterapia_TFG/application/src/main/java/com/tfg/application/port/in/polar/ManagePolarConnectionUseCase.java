package com.tfg.application.port.in.polar;

import com.tfg.model.patient.PatientId;

public interface ManagePolarConnectionUseCase {
    String initiateConnection(PatientId patientId);
    void finalizeConnection(String code, PatientId patientId);
}
