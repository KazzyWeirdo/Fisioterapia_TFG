package com.tfg.application.service.statistics;

import com.tfg.model.patient.Patient;
import com.tfg.application.port.in.statistics.GetPathologyRehabilitationStatsUseCase;
import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.model.statistics.PathologyRehabilitationStats;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetPathologyRehabilitationStatsService implements GetPathologyRehabilitationStatsUseCase {

    private final PatientRepository patientRepository;

    public GetPathologyRehabilitationStatsService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<PathologyRehabilitationStats> getPathologyRehabilitationStats() {
        List<Patient> discharged = patientRepository.findAllDischarged();

        Map<String, List<Patient>> byPathology = discharged.stream()
                .filter(p -> p.getPathology() != null)
                .collect(Collectors.groupingBy(p -> p.getPathology().name()));

        return byPathology.entrySet().stream()
                .map(entry -> {
                    List<Patient> group = entry.getValue();
                    double avgDays = group.stream()
                            .mapToLong(p -> ChronoUnit.DAYS.between(p.getRegistrationDate(), p.getDischargeDate()))
                            .average().orElse(0);
                    return new PathologyRehabilitationStats(entry.getKey(), avgDays, group.size());
                })
                .sorted(Comparator.comparing(PathologyRehabilitationStats::pathology))
                .collect(Collectors.toList());
    }
}
