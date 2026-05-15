package com.tfg.application.service.statistics;

import com.tfg.model.indiba.IndibaSession;
import com.tfg.model.patient.PatientId;
import com.tfg.application.port.in.statistics.GetIndibaSessionStatsUseCase;
import com.tfg.application.port.out.persistence.IndibaSessionRepository;
import com.tfg.model.statistics.IndibaSessionStats;

import java.util.*;
import java.util.stream.Collectors;

public class GetIndibaSessionStatsService implements GetIndibaSessionStatsUseCase {

    private final IndibaSessionRepository indibaSessionRepository;

    public GetIndibaSessionStatsService(IndibaSessionRepository indibaSessionRepository) {
        this.indibaSessionRepository = indibaSessionRepository;
    }

    @Override
    public IndibaSessionStats getIndibaSessionStats(PatientId patientId) {
        List<IndibaSession> sessions = indibaSessionRepository.findAllByPatientId(patientId);
        if (sessions.isEmpty()) {
            return new IndibaSessionStats(0, 0, null, null, null, Map.of());
        }

        double avgDuration = sessions.stream()
                .mapToLong(s -> (s.getEndSession().getTime() - s.getBeginSession().getTime()) / 60_000)
                .average().orElse(0);

        String mostTreatedArea = sessions.stream()
                .collect(Collectors.groupingBy(IndibaSession::getTreatedArea, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);

        OptionalDouble avgCap = sessions.stream()
                .filter(s -> s.getCapacitiveIntensity() != null)
                .mapToDouble(IndibaSession::getCapacitiveIntensity)
                .average();

        OptionalDouble avgRes = sessions.stream()
                .filter(s -> s.getResistiveIntensity() != null)
                .mapToDouble(IndibaSession::getResistiveIntensity)
                .average();

        Map<String, Long> modeDist = sessions.stream()
                .collect(Collectors.groupingBy(s -> s.getMode().name(), Collectors.counting()));

        return new IndibaSessionStats(
                sessions.size(),
                avgDuration,
                mostTreatedArea,
                avgCap.isPresent() ? avgCap.getAsDouble() : null,
                avgRes.isPresent() ? avgRes.getAsDouble() : null,
                modeDist
        );
    }
}
