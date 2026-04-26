package com.tfg.port.in.trainingsession;

import com.tfg.trainingsession.TrainingSession;

import java.util.List;

public interface GetAllTrainingSessionsForExportUseCase {
    List<TrainingSession> getAllTrainingSessionsForExport();
}
