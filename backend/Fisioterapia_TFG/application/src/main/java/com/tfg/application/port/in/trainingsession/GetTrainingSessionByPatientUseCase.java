package com.tfg.application.port.in.trainingsession;

import com.tfg.model.patient.PatientId;
import com.tfg.application.pojos.pagedpojos.PageQuery;
import com.tfg.application.pojos.pagedpojos.PagedResponse;
import com.tfg.application.pojos.query.TrainingSessionSummaryElement;

public interface GetTrainingSessionByPatientUseCase {

    PagedResponse<TrainingSessionSummaryElement> getTrainingSessionFromPatient(PageQuery query, PatientId patientId);
}
