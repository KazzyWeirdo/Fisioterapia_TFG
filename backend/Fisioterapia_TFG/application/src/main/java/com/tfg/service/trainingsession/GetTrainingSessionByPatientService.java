package com.tfg.service.trainingsession;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.exceptions.InvalidPageOrSizeException;
import com.tfg.patient.PatientId;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.TrainingSessionSummaryElement;
import com.tfg.port.in.trainingsession.GetTrainingSessionByPatientUseCase;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.TrainingSessionRepository;

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
