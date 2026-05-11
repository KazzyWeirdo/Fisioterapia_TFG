package com.tfg.application.service.patient;

import com.tfg.application.exceptions.InvalidPageOrSizeException;
import com.tfg.application.pojos.pagedpojos.PageQuery;
import com.tfg.application.pojos.pagedpojos.PagedResponse;
import com.tfg.application.pojos.query.PatientSummaryElement;
import com.tfg.application.port.in.patient.GetAllPatientsUseCase;
import com.tfg.application.port.out.persistence.PatientRepository;

public class GetAllPatientsService implements GetAllPatientsUseCase {
    private final PatientRepository patientRepository;

    public GetAllPatientsService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }


    @Override
    public PagedResponse<PatientSummaryElement> getAllPatients(PageQuery query) {
        if (query.page() < 0 || query.size() <= 0) {
            throw new InvalidPageOrSizeException();
        }

        return patientRepository.findAllSummaries(query);
    }
}
