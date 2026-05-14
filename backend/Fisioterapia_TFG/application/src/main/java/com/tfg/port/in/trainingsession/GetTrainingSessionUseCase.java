package com.tfg.port.in.trainingsession;

import com.tfg.trainingsession.TrainingSession;
import com.tfg.trainingsession.TrainingSessionId;

public interface GetTrainingSessionUseCase {

    TrainingSession getTrainingSession(TrainingSessionId id);
}
