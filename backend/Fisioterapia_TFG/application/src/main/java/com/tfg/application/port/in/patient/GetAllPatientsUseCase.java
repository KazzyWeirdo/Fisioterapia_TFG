package com.tfg.application.port.in.patient;

import com.tfg.application.pojos.pagedpojos.PageQuery;
import com.tfg.application.pojos.pagedpojos.PagedResponse;
import com.tfg.application.pojos.query.PatientSummaryElement;

public interface GetAllPatientsUseCase {
    PagedResponse<PatientSummaryElement> getAllPatients(PageQuery query);
}
