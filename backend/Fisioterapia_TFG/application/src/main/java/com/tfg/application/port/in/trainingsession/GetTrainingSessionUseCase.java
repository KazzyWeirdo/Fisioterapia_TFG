package com.tfg.application.port.in.trainingsession;

import com.tfg.model.trainingsession.TrainingSession;
import com.tfg.model.trainingsession.TrainingSessionId;

public interface GetTrainingSessionUseCase {

    TrainingSession getTrainingSession(TrainingSessionId id);
}
