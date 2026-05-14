package com.tfg.application.service.trainingsession;

import com.tfg.application.port.in.trainingsession.GetAllTrainingSessionsForExportUseCase;
import com.tfg.application.port.out.persistence.TrainingSessionRepository;
import com.tfg.model.trainingsession.TrainingSession;

import java.util.List;

public class GetAllTrainingSessionsForExportService implements GetAllTrainingSessionsForExportUseCase {

    private final TrainingSessionRepository trainingSessionRepository;

    public GetAllTrainingSessionsForExportService(TrainingSessionRepository trainingSessionRepository) {
        this.trainingSessionRepository = trainingSessionRepository;
    }

    @Override
    public List<TrainingSession> getAllTrainingSessionsForExport() {
        return trainingSessionRepository.findAllForExport();
    }
}
