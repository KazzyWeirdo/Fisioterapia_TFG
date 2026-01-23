package com.tfg.port.out.persistence;

import com.tfg.patient.Patient;

import java.util.Optional;

public interface PatientRepository {

    void save(Patient patient);

    Optional<Patient> findById(Long id);

    Optional<Patient> findByEmail(String email);
}
