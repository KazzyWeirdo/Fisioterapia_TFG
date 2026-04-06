package com.tfg.port.in.pni;

import com.tfg.patient.PatientId;
import com.tfg.pni.PniReport;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.PniReportSummaryElement;

import java.util.List;

public interface GetPniReportsFromPatientUseCase {
    PagedResponse<PniReportSummaryElement> getPniReportsFromPatient(PageQuery query, PatientId patientId);
}
