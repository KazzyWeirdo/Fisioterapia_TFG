package com.tfg.service.trainingsession;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.port.in.trainingsession.GetTrainingSessionUseCase;
import com.tfg.port.out.persistence.TrainingSessionRepository;
import com.tfg.trainingsession.TrainingSession;
import com.tfg.trainingsession.TrainingSessionId;

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
