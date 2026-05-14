package com.tfg.service.indiba;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.exceptions.InvalidPageOrSizeException;
import com.tfg.patient.PatientId;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.IndibaSummaryElement;
import com.tfg.port.in.indiba.GetIndibaSessionFromPatientUseCase;
import com.tfg.port.out.persistence.IndibaSessionRepository;
import com.tfg.port.out.persistence.PatientRepository;

public class GetIndibaSessionFromPatientService implements GetIndibaSessionFromPatientUseCase {

    private final IndibaSessionRepository indibaSessionRepository;
    private final PatientRepository patientRepository;

    public GetIndibaSessionFromPatientService(IndibaSessionRepository indibaSessionRepository, PatientRepository patientRepository) {
        this.indibaSessionRepository = indibaSessionRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public PagedResponse<IndibaSummaryElement> getIndibaSessionsFromPatient(PageQuery query, PatientId patientId) {
        patientRepository.findById(patientId)
                .orElseThrow(InvalidIdException::new);

        if (query.page() < 0 || query.size() <= 0) {
            throw new InvalidPageOrSizeException();
        }

        return indibaSessionRepository.findAllByPatientId(query, patientId);
    }
}
