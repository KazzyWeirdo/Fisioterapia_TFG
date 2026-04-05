package com.tfg.port.in.patient;

import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.PatientSummaryElement;

public interface GetAllPatientsUseCase {
    PagedResponse<PatientSummaryElement> getAllPatients(PageQuery query);
}
