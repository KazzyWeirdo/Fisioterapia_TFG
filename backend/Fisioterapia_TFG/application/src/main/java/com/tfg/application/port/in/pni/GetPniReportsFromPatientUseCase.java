package com.tfg.application.port.in.pni;

import com.tfg.model.patient.PatientId;
import com.tfg.application.pojos.pagedpojos.PageQuery;
import com.tfg.application.pojos.pagedpojos.PagedResponse;
import com.tfg.application.pojos.query.PniReportSummaryElement;

public interface GetPniReportsFromPatientUseCase {
    PagedResponse<PniReportSummaryElement> getPniReportsFromPatient(PageQuery query, PatientId patientId);
}
