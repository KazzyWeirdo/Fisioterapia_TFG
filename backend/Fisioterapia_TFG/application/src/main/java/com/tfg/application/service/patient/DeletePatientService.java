package com.tfg.application.service.patient;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.application.port.in.patient.DeletePatientUseCase;
import com.tfg.application.port.out.persistence.IndibaSessionRepository;
import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.application.port.out.persistence.PniReportRepository;
import com.tfg.application.port.out.persistence.TrainingSessionRepository;
import com.tfg.model.patient.PatientId;

public class DeletePatientService implements DeletePatientUseCase {

    private final PatientRepository patientRepository;
    private final IndibaSessionRepository indibaSessionRepository;
    private final PniReportRepository pniReportRepository;
    private final TrainingSessionRepository trainingSessionRepository;

    public DeletePatientService(
            PatientRepository patientRepository,
            IndibaSessionRepository indibaSessionRepository,
            PniReportRepository pniReportRepository,
            TrainingSessionRepository trainingSessionRepository) {
        this.patientRepository = patientRepository;
        this.indibaSessionRepository = indibaSessionRepository;
        this.pniReportRepository = pniReportRepository;
        this.trainingSessionRepository = trainingSessionRepository;
    }

    @Override
    public void delete(PatientId patientId) {
        patientRepository.findById(patientId)
                .orElseThrow(InvalidIdException::new);

        indibaSessionRepository.deleteAllByPatientId(patientId);
        pniReportRepository.deleteAllByPatientId(patientId);
        trainingSessionRepository.deleteAllByPatientId(patientId);
        patientRepository.deleteById(patientId);
    }
}
