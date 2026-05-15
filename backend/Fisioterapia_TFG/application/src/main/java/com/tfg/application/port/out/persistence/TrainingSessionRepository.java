package com.tfg.application.port.out.persistence;

import com.tfg.model.patient.PatientId;
import com.tfg.application.pojos.pagedpojos.PageQuery;
import com.tfg.application.pojos.pagedpojos.PagedResponse;
import com.tfg.application.pojos.query.TrainingSessionSummaryElement;
import com.tfg.model.trainingsession.TrainingSession;
import com.tfg.model.trainingsession.TrainingSessionId;

import java.util.List;
import java.util.Optional;

public interface TrainingSessionRepository {

    void save(TrainingSession trainingSession);

    void deleteAll();

    void deleteAllByPatientId(PatientId patientId);

    Optional<TrainingSession> findById (TrainingSessionId id);

    PagedResponse<TrainingSessionSummaryElement> findAllByPatientId (PageQuery query, PatientId patientId);

    List<Object[]> countSessionByMonth (PatientId patientId, Integer year);

    List<Object[]> calculateRpeProgression (PatientId patientId, String exerciseName);

    List<TrainingSession> findAllForExport();
}
