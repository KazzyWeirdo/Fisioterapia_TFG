package com.tfg.service.statistics;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.indiba.IndibaSession;
import com.tfg.patient.PatientId;
import com.tfg.port.in.statistics.GetPatientTransitionRatioUseCase;
import com.tfg.port.out.persistence.IndibaSessionRepository;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.TrainingSessionRepository;
import com.tfg.statistics.PatientMonthTransitionRatio;
import com.tfg.trainingsession.TrainingSession;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    public List<PatientMonthTransitionRatio> getTransitionRatio(PatientId id) {
        patientRepository.findById(id)
                .orElseThrow(InvalidIdException::new);

        List<IndibaSession> indibaSessions = indibaSessionRepository.countSessionGroupedByMonth(id);
        List<TrainingSession> trainingSessions = trainingSessionRepository.countSessionByMonth(id);

        List<PatientMonthTransitionRatio> ratios = new ArrayList<>();

        for (int i = 2010; i <= LocalDate.now().getYear(); i++) {
            int finalI = i;
            for (int j = 1; j <= 12; j++) {
                if (j > LocalDate.now().getMonthValue() && i == LocalDate.now().getYear()) break;
                int finalJ = j;
                long indibaCount = indibaSessions.stream()
                        .filter(s -> s.getBeginSession().getYear() == finalI && s.getBeginSession().getMonth() == finalJ)
                        .count();

                long trainingCount = trainingSessions.stream()
                        .filter(s -> s.getDate().getYear() == finalI && s.getDate().getMonthValue() == finalJ)
                        .count();

                if(indibaCount == 0 && trainingCount == 0) continue;

                ratios.add(new PatientMonthTransitionRatio(j, i, indibaCount, trainingCount));
            }
        }

        return ratios;
    }
}
