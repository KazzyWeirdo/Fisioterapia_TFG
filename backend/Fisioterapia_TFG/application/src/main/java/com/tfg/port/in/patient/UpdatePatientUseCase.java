package com.tfg.port.in.patient;

import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;

public interface UpdatePatientUseCase {
    void updatePatient(PatientId id, Patient patient);
}
