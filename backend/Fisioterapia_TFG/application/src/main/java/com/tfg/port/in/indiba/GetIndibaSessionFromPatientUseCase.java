package com.tfg.port.in.indiba;

import com.tfg.patient.PatientId;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.IndibaSummaryElement;

public interface GetIndibaSessionFromPatientUseCase {
    PagedResponse<IndibaSummaryElement> getIndibaSessionsFromPatient(PageQuery query, PatientId patientId);
}
