package com.tfg.service.trainingsession;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.patient.PatientId;
import com.tfg.port.in.trainingsession.GetTrainingSessionByPatientUseCase;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.TrainingSessionRepository;
import com.tfg.trainingsession.TrainingSession;

import java.util.List;

public class GetTrainingSessionByPatientService implements GetTrainingSessionByPatientUseCase {

    private final TrainingSessionRepository trainingSessionRepository;
    private final PatientRepository patientRepository;

    public GetTrainingSessionByPatientService(TrainingSessionRepository trainingSessionRepository, PatientRepository patientRepository) {
        this.trainingSessionRepository = trainingSessionRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public List<TrainingSession> getTrainingSessionFromPatient(PatientId patientId) {
        patientRepository.findById(patientId)
                .orElseThrow(InvalidIdException::new);

        return trainingSessionRepository.findAllByPatientId(patientId);
    }
}
