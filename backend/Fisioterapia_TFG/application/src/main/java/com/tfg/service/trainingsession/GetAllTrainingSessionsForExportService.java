package com.tfg.service.trainingsession;

import com.tfg.port.in.trainingsession.GetAllTrainingSessionsForExportUseCase;
import com.tfg.port.out.persistence.TrainingSessionRepository;
import com.tfg.trainingsession.TrainingSession;

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
