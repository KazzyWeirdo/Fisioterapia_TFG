package com.tfg.service.pni;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.exceptions.InvalidPageOrSizeException;
import com.tfg.patient.PatientId;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.PniReportSummaryElement;
import com.tfg.port.in.pni.GetPniReportsFromPatientUseCase;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.PniReportRepository;

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
