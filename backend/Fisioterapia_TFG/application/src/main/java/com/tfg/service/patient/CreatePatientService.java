package com.tfg.service.patient;

import com.tfg.patient.*;
import com.tfg.port.in.patient.CreatePatientUseCase;
import com.tfg.port.out.persistence.PatientRepository;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class CreatePatientService implements CreatePatientUseCase {

    private final PatientRepository patientRepository;

    public CreatePatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public void createPatient(String email, String dni, PatientGender gender, String name, String surname, String secondSurname, LocalDate dateOfBirth, int phoneNumber) {
        patientRepository.findByEmail(new PatientEmail(email)).ifPresent(p -> {
            throw new IllegalArgumentException("Patient with email " + email + " already exists");
        });

        patientRepository.findByDni(new PatientDNI(dni)).ifPresent(p -> {
            throw new IllegalArgumentException("Patient with DNI " + dni + " already exists");
        });

        Patient patient = new Patient(new PatientId(ThreadLocalRandom.current().nextInt(1_000_000)),
                new PatientEmail(email),
                new PatientDNI(dni),
                gender,
                name,
                surname,
                secondSurname,
                dateOfBirth,
                phoneNumber
                );
        patientRepository.save(patient);
    }
}
