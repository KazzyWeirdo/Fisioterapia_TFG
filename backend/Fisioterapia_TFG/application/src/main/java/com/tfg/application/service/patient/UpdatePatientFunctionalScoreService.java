package com.tfg.application.service.patient;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.model.patient.Patient;
import com.tfg.model.patient.PatientId;
import com.tfg.application.port.in.patient.UpdatePatientFunctionalScoreUseCase;
import com.tfg.application.port.out.persistence.PatientRepository;

public class UpdatePatientFunctionalScoreService implements UpdatePatientFunctionalScoreUseCase {

    private final PatientRepository patientRepository;

    public UpdatePatientFunctionalScoreService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public void updateFunctionalScore(PatientId patientId, int score) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(InvalidIdException::new);
        patient.applyFunctionalScore(score);
        patientRepository.save(patient);
    }
}
