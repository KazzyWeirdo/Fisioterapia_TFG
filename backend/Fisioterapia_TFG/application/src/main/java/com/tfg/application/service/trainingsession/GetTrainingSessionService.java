package com.tfg.application.service.trainingsession;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.application.port.in.trainingsession.GetTrainingSessionUseCase;
import com.tfg.application.port.out.persistence.TrainingSessionRepository;
import com.tfg.model.trainingsession.TrainingSession;
import com.tfg.model.trainingsession.TrainingSessionId;

public class GetTrainingSessionService implements GetTrainingSessionUseCase {

    private final TrainingSessionRepository trainingSessionRepository;

    public GetTrainingSessionService(TrainingSessionRepository trainingSessionRepository) {
        this.trainingSessionRepository = trainingSessionRepository;
    }

    @Override
    public TrainingSession getTrainingSession(TrainingSessionId id) {
        return trainingSessionRepository.findById(id)
                .orElseThrow(InvalidIdException::new);
    }
}
