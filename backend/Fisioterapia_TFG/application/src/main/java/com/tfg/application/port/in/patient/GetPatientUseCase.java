package com.tfg.application.port.in.patient;

import com.tfg.model.patient.Patient;
import com.tfg.model.patient.PatientId;

public interface GetPatientUseCase {
    Patient getPatient(PatientId id);
}
