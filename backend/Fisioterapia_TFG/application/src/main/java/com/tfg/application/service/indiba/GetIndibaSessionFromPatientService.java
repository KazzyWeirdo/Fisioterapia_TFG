package com.tfg.application.service.indiba;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.application.exceptions.InvalidPageOrSizeException;
import com.tfg.model.patient.PatientId;
import com.tfg.application.pojos.pagedpojos.PageQuery;
import com.tfg.application.pojos.pagedpojos.PagedResponse;
import com.tfg.application.pojos.query.IndibaSummaryElement;
import com.tfg.application.port.in.indiba.GetIndibaSessionFromPatientUseCase;
import com.tfg.application.port.out.persistence.IndibaSessionRepository;
import com.tfg.application.port.out.persistence.PatientRepository;

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
