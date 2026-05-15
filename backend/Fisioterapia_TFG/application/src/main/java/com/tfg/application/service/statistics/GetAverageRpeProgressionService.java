package com.tfg.application.service.statistics;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientId;
import com.tfg.application.port.in.statistics.GetAverageRpeProgressionUseCase;
import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.application.port.out.persistence.TrainingSessionRepository;
import com.tfg.model.statistics.AverageRpeProgression;

import java.util.List;
import java.util.stream.Collectors;

public class GetAverageRpeProgressionService implements GetAverageRpeProgressionUseCase {

    private final TrainingSessionRepository trainingSessionRepository;
    private final PatientRepository patientRepository;

    public GetAverageRpeProgressionService(TrainingSessionRepository trainingSessionRepository, PatientRepository patientRepository) {
        this.trainingSessionRepository = trainingSessionRepository;
        this.patientRepository = patientRepository;
    }


    @Override
    public List<AverageRpeProgression> calculateProgression(PatientId patientId, String exerciseName) {
        patientRepository.findById(patientId)
                .orElseThrow(InvalidIdException::new);

        List<Object[]> rawData = trainingSessionRepository.calculateRpeProgression(patientId, exerciseName);

        return rawData.stream()
                .map(row -> {
                    java.sql.Date sqlDate = (java.sql.Date) row[0];
                    Double averageRpe = ((Number) row[1]).doubleValue();

                    return new AverageRpeProgression(sqlDate.toLocalDate(), averageRpe);
                })
                .collect(Collectors.toList());
    }
}
