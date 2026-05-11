package com.tfg.application.service.patient;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.model.patient.Patient;
import com.tfg.model.patient.PatientId;
import com.tfg.application.port.in.patient.DischargePatientUseCase;
import com.tfg.application.port.out.persistence.PatientRepository;

import java.time.LocalDate;

public class DischargePatientService implements DischargePatientUseCase {

    private final PatientRepository patientRepository;

    public DischargePatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public void discharge(PatientId patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(InvalidIdException::new);
        patient.discharge(LocalDate.now());
        patientRepository.save(patient);
    }
}
