package com.tfg.service.patient;

import com.tfg.patient.Patient;
import com.tfg.port.in.patient.GetPatientUseCase;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.patient.PatientId;

public class GetPatientService implements GetPatientUseCase {

    private final PatientRepository patientRepository;

    public GetPatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient getPatient(PatientId id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("Patient not found"));
    }
}
