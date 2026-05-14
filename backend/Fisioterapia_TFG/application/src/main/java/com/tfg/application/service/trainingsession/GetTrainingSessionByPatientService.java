package com.tfg.application.service.trainingsession;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.application.exceptions.InvalidPageOrSizeException;
import com.tfg.model.patient.PatientId;
import com.tfg.application.pojos.pagedpojos.PageQuery;
import com.tfg.application.pojos.pagedpojos.PagedResponse;
import com.tfg.application.pojos.query.TrainingSessionSummaryElement;
import com.tfg.application.port.in.trainingsession.GetTrainingSessionByPatientUseCase;
import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.application.port.out.persistence.TrainingSessionRepository;

public class GetTrainingSessionByPatientService implements GetTrainingSessionByPatientUseCase {

    private final TrainingSessionRepository trainingSessionRepository;
    private final PatientRepository patientRepository;

    public GetTrainingSessionByPatientService(TrainingSessionRepository trainingSessionRepository, PatientRepository patientRepository) {
        this.trainingSessionRepository = trainingSessionRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public PagedResponse<TrainingSessionSummaryElement> getTrainingSessionFromPatient(PageQuery query, PatientId patientId) {
        patientRepository.findById(patientId)
                .orElseThrow(InvalidIdException::new);

        if (query.page() < 0 || query.size() <= 0) {
            throw new InvalidPageOrSizeException();
        }

        return trainingSessionRepository.findAllByPatientId(query, patientId);
    }
}
