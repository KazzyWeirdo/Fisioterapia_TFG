package com.tfg.service.trainingsession;

import com.tfg.port.in.trainingsession.CreateTrainingSessionUseCase;
import com.tfg.port.out.persistence.TrainingSessionRepository;
import com.tfg.trainingsession.TrainingSession;

public class CreateTrainingSessionService implements CreateTrainingSessionUseCase {

        private final TrainingSessionRepository trainingSessionRepository;

        public CreateTrainingSessionService(TrainingSessionRepository trainingSessionRepository) {
            this.trainingSessionRepository = trainingSessionRepository;
        }

        @Override
        public void createTrainingSession(TrainingSession trainingSession) {
            trainingSessionRepository.save(trainingSession);
        }
}
