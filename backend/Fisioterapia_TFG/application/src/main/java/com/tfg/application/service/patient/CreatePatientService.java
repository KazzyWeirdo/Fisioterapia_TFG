package com.tfg.application.service.patient;

import com.tfg.model.patient.Patient;
import com.tfg.application.port.in.patient.CreatePatientUseCase;
import com.tfg.application.port.out.persistence.PatientRepository;

public class CreatePatientService implements CreatePatientUseCase {

    private final PatientRepository patientRepository;

    public CreatePatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public void createPatient(Patient patient) {
        patientRepository.findByEmail(patient.getEmail()).ifPresent(p -> {
            throw new IllegalArgumentException("Patient with email " + patient.getEmail().value() + " already exists");
        });

        patientRepository.findByDni(patient.getDni()).ifPresent(p -> {
            throw new IllegalArgumentException("Patient with DNI " + patient.getDni().value() + " already exists");
        });

        patientRepository.save(patient);
    }
}
