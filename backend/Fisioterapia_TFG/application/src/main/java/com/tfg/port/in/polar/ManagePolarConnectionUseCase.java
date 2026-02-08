package com.tfg.port.in.polar;

import com.tfg.patient.PatientId;

public interface ManagePolarConnectionUseCase {
    String initiateConnection(PatientId patientId);
    void finalizeConnection(String code, PatientId patientId);
}
