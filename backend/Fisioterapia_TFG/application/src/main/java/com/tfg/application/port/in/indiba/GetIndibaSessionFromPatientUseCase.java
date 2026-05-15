package com.tfg.application.port.in.indiba;

import com.tfg.model.patient.PatientId;
import com.tfg.application.pojos.pagedpojos.PageQuery;
import com.tfg.application.pojos.pagedpojos.PagedResponse;
import com.tfg.application.pojos.query.IndibaSummaryElement;

public interface GetIndibaSessionFromPatientUseCase {
    PagedResponse<IndibaSummaryElement> getIndibaSessionsFromPatient(PageQuery query, PatientId patientId);
}
