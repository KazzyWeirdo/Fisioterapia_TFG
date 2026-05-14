package com.tfg.port.in.trainingsession;

import com.tfg.patient.PatientId;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.TrainingSessionSummaryElement;

public interface GetTrainingSessionByPatientUseCase {

    PagedResponse<TrainingSessionSummaryElement> getTrainingSessionFromPatient(PageQuery query, PatientId patientId);
}
