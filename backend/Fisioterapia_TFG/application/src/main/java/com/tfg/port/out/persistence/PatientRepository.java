package com.tfg.port.out.persistence;

import com.tfg.patient.Patient;
import com.tfg.patient.PatientDNI;
import com.tfg.patient.PatientEmail;
import com.tfg.patient.PatientId;

import java.util.Optional;

public interface PatientRepository {

    void save(Patient patient);

    Optional<Patient> findById(PatientId id);

    Optional<Patient> findByEmail(PatientEmail email);

    Optional<Patient> findByDni(PatientDNI dni);
}
