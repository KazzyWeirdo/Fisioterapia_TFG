package com.tfg.application.port.in.patient;

import com.tfg.model.patient.PatientId;

public interface UpdatePatientFunctionalScoreUseCase {
    void updateFunctionalScore(PatientId patientId, int score);
}
