package com.tfg.application.port.in.trainingsession;

import com.tfg.model.trainingsession.TrainingSession;

import java.util.List;

public interface GetAllTrainingSessionsForExportUseCase {
    List<TrainingSession> getAllTrainingSessionsForExport();
}
