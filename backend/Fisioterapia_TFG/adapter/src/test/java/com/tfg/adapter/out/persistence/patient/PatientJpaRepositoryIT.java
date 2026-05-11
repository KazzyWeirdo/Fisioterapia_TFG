package com.tfg.adapter.out.persistence.patient;

import com.tfg.adapter.out.persistence.BaseRepositoryIT;
import com.tfg.model.patient.Pathology;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.patient.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PatientJpaRepositoryIT extends BaseRepositoryIT {

    @Autowired
    private PatientJpaRepository patientJpaRepository;

    @Autowired
    private PatientJpaDataRepository patientJpaDataRepository;

    @AfterEach
    void tearDown() {
        patientJpaDataRepository.deleteAll();
    }

    @Test
    public void givenMixedPatients_whenFindAllDischarged_thenReturnOnlyDischarged() {
        Patient active = PatientFactory.createTestPatient("active@test.com", "11111111A");
        Patient discharged = PatientFactory.createTestPatient("discharged@test.com", "22222222B");
        discharged.discharge(LocalDate.now());

        patientJpaRepository.save(active);
        patientJpaRepository.save(discharged);

        List<Patient> result = patientJpaRepository.findAllDischarged();

        assertEquals(1, result.size());
        assertEquals("discharged@test.com", result.get(0).getEmail().value());
        assertNotNull(result.get(0).getDischargeDate());
    }

    @Test
    public void givenNoDischargedPatients_whenFindAllDischarged_thenReturnEmptyList() {
        patientJpaRepository.save(PatientFactory.createTestPatient("only@test.com", "33333333C"));

        List<Patient> result = patientJpaRepository.findAllDischarged();

        assertTrue(result.isEmpty());
    }

    @Test
    public void givenPatientWithPathology_whenSaveAndLoad_thenPathologyPersisted() {
        Patient patient = PatientFactory.createTestPatient("patho@test.com", "44444444D");
        patient.setPathology(Pathology.KNEE_OSTEOARTHRITIS);

        patientJpaRepository.save(patient);
        Patient loaded = patientJpaRepository.findByEmail(patient.getEmail()).orElseThrow();

        assertEquals(Pathology.KNEE_OSTEOARTHRITIS, loaded.getPathology());
        assertNotNull(loaded.getRegistrationDate());
    }
}
