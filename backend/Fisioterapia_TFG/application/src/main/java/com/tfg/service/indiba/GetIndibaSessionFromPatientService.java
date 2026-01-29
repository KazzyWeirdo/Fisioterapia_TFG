package com.tfg.service.indiba;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.indiba.IndibaSession;
import com.tfg.patient.PatientId;
import com.tfg.port.in.indiba.GetIndibaSessionFromPatientUseCase;
import com.tfg.port.out.persistence.IndibaSessionRepository;
import com.tfg.port.out.persistence.PatientRepository;

import java.util.List;

public class GetIndibaSessionFromPatientService implements GetIndibaSessionFromPatientUseCase {

    private final IndibaSessionRepository indibaSessionRepository;
    private final PatientRepository patientRepository;

    public GetIndibaSessionFromPatientService(IndibaSessionRepository indibaSessionRepository, PatientRepository patientRepository) {
        this.indibaSessionRepository = indibaSessionRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public List<IndibaSession> getIndibaSessionsFromPatient(PatientId patientId) {
        patientRepository.findById(patientId)
                .orElseThrow(InvalidIdException::new);

        return indibaSessionRepository.findAllByPatientId(patientId);
    }
}
