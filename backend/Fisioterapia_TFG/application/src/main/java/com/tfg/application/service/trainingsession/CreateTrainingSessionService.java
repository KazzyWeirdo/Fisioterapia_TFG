package com.tfg.application.service.trainingsession;

import com.tfg.application.port.in.trainingsession.CreateTrainingSessionUseCase;
import com.tfg.application.port.out.persistence.TrainingSessionRepository;
import com.tfg.model.trainingsession.TrainingSession;

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
