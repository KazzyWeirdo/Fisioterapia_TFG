package com.tfg.application.service.pni;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.application.exceptions.InvalidPageOrSizeException;
import com.tfg.model.patient.PatientId;
import com.tfg.application.pojos.pagedpojos.PageQuery;
import com.tfg.application.pojos.pagedpojos.PagedResponse;
import com.tfg.application.pojos.query.PniReportSummaryElement;
import com.tfg.application.port.in.pni.GetPniReportsFromPatientUseCase;
import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.application.port.out.persistence.PniReportRepository;

public class GetPniReportsFromPatientService implements GetPniReportsFromPatientUseCase {

    private final PniReportRepository pniReportRepository;
    private final PatientRepository patientRepository;

    public GetPniReportsFromPatientService(PniReportRepository pniReportRepository, PatientRepository patientRepository) {
        this.pniReportRepository = pniReportRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public PagedResponse<PniReportSummaryElement> getPniReportsFromPatient(PageQuery query, PatientId patientId) {
        patientRepository.findById(patientId)
                .orElseThrow(InvalidIdException::new);

        if (query.page() < 0 || query.size() <= 0) {
            throw new InvalidPageOrSizeException();
        }

        return pniReportRepository.findAllReportsByPatiendId(query, patientId);
    }
}
