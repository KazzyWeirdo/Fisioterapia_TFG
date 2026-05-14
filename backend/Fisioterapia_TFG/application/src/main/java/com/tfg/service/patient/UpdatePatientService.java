package com.tfg.service.patient;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import com.tfg.port.in.patient.UpdatePatientUseCase;
import com.tfg.port.out.persistence.PatientRepository;

public class UpdatePatientService implements UpdatePatientUseCase {

    private final PatientRepository patientRepository;

    public UpdatePatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }


    @Override
    public void updatePatient(PatientId id, Patient patient) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(InvalidIdException::new);

        if (!existingPatient.getEmail().value().equals(patient.getEmail().value())) {
            patientRepository.findByEmail(patient.getEmail()).ifPresent(p -> {
                throw new IllegalArgumentException("Patient with email " + patient.getEmail().value() + " already exists");
            });
        }

        if (!existingPatient.getDni().value().equals(patient.getDni().value())){
            patientRepository.findByDni(patient.getDni()).ifPresent(p -> {
                throw new IllegalArgumentException("Patient with DNI " + patient.getDni().value() + " already exists");
            });
        }

        patientRepository.update(id, patient);
    }
}
