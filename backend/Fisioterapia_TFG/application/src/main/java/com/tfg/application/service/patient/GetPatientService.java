package com.tfg.application.service.patient;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.model.patient.Patient;
import com.tfg.application.port.in.patient.GetPatientUseCase;
import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.model.patient.PatientId;

public class GetPatientService implements GetPatientUseCase {

    private final PatientRepository patientRepository;

    public GetPatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient getPatient(PatientId id) {
        return patientRepository.findById(id)
                .orElseThrow(InvalidIdException::new);
    }
}
