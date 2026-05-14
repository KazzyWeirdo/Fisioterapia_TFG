package com.tfg.port.out.persistence;

import com.tfg.patient.PatientId;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.TrainingSessionSummaryElement;
import com.tfg.trainingsession.TrainingSession;
import com.tfg.trainingsession.TrainingSessionId;

import java.util.List;
import java.util.Optional;

public interface TrainingSessionRepository {

    void save(TrainingSession trainingSession);

    void deleteAll();

    Optional<TrainingSession> findById (TrainingSessionId id);

    PagedResponse<TrainingSessionSummaryElement> findAllByPatientId (PageQuery query, PatientId patientId);

    List<Object[]> countSessionByMonth (PatientId patientId, Integer year);

    List<Object[]> calculateVolumeProgression (PatientId patientId, String exerciseName);

    List<TrainingSession> findAllForExport();
}
