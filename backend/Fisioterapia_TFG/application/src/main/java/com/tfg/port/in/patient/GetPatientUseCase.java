package com.tfg.port.in.patient;

import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;

public interface GetPatientUseCase {
    Patient getPatient(PatientId id);
}
