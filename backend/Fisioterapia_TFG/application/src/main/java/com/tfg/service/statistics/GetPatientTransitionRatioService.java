package com.tfg.service.statistics;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.patient.PatientId;
import com.tfg.port.in.statistics.GetPatientTransitionRatioUseCase;
import com.tfg.port.out.persistence.IndibaSessionRepository;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.TrainingSessionRepository;
import com.tfg.statistics.PatientMonthTransitionRatio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetPatientTransitionRatioService implements GetPatientTransitionRatioUseCase {

    private final PatientRepository patientRepository;
    private final IndibaSessionRepository indibaSessionRepository;
    private final TrainingSessionRepository trainingSessionRepository;

    public GetPatientTransitionRatioService(PatientRepository patientRepository,
                                            IndibaSessionRepository indibaSessionRepository,
                                            TrainingSessionRepository trainingSessionRepository) {
        this.patientRepository = patientRepository;
        this.indibaSessionRepository = indibaSessionRepository;
        this.trainingSessionRepository = trainingSessionRepository;
    }
    @Override
    public List<PatientMonthTransitionRatio> getTransitionRatio(PatientId id, Integer year) {
        patientRepository.findById(id)
                .orElseThrow(InvalidIdException::new);

        List<PatientMonthTransitionRatio> ratios = new ArrayList<>();

        List<Object[]> indibaSessionsCount = indibaSessionRepository.countSessionGroupedByMonth(id, year);
        List<Object[]> trainingSessionsCount = trainingSessionRepository.countSessionByMonth(id, year);
        if(indibaSessionsCount.isEmpty() && trainingSessionsCount.isEmpty()) return List.of(); // Si no hay sesiones, se devuelve una lista vacía

        Map<Integer, Long> indibaSessionsMap = parseToMap(indibaSessionsCount);
        Map<Integer, Long> trainingSessionsMap = parseToMap(trainingSessionsCount);

        for (int month = 1; month <= 12; month++) {
            if (month > LocalDate.now().getMonthValue() && year == LocalDate.now().getYear()) break;
            long indibaCount = indibaSessionsMap.getOrDefault(month, 0L);
            long trainingCount = trainingSessionsMap.getOrDefault(month, 0L);

            ratios.add(new PatientMonthTransitionRatio(month, year, indibaCount, trainingCount));
        }

        return ratios;
    }

    private Map<Integer, Long> parseToMap(List<Object[]> sessions) {
        return sessions.stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                session -> ((Number) session[0]).intValue(),
                                session -> ((Number) session[1]).longValue()
                        )
                );
    }
}
