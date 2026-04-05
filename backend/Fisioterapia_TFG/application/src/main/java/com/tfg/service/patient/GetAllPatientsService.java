package com.tfg.service.patient;

import com.tfg.exceptions.InvalidPageOrSizeException;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.PatientSummaryElement;
import com.tfg.port.in.patient.GetAllPatientsUseCase;
import com.tfg.port.out.persistence.PatientRepository;

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
