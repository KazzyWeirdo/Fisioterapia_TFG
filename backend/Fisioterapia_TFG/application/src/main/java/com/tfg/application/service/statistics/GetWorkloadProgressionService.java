package com.tfg.application.service.statistics;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientId;
import com.tfg.application.port.in.statistics.GetWorkloadProgressionUseCase;
import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.application.port.out.persistence.TrainingSessionRepository;
import com.tfg.model.statistics.WorkloadProgression;

import java.util.List;
import java.util.stream.Collectors;

public class GetWorkloadProgressionService implements GetWorkloadProgressionUseCase {

    private final TrainingSessionRepository trainingSessionRepository;
    private final PatientRepository patientRepository;

    public GetWorkloadProgressionService(TrainingSessionRepository trainingSessionRepository, PatientRepository patientRepository) {
        this.trainingSessionRepository = trainingSessionRepository;
        this.patientRepository = patientRepository;
    }


    @Override
    public List<WorkloadProgression> calculateProgression(PatientId patientId, String exerciseName) {
        patientRepository.findById(patientId)
                .orElseThrow(InvalidIdException::new);

        List<Object[]> rawData = trainingSessionRepository.calculateVolumeProgression(patientId, exerciseName);

        return rawData.stream()
                .map(row -> {
                    java.sql.Date sqlDate = (java.sql.Date) row[0];
                    Double volume = ((Number) row[1]).doubleValue();

                    return new WorkloadProgression(sqlDate.toLocalDate(), volume);
                })
                .collect(Collectors.toList());
    }
}
