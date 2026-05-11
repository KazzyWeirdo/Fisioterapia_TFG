package com.tfg.adapter.out.persistence.indiba;

import com.tfg.adapter.out.persistence.BaseRepositoryIT;
import com.tfg.adapter.out.persistence.patient.PatientJpaDataRepository;
import com.tfg.adapter.out.persistence.patient.PatientJpaRepository;
import com.tfg.model.indiba.IndibaSession;
import com.tfg.model.indiba.IndibaSessionFactory;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import com.tfg.model.patient.Patient;
import com.tfg.model.patient.PatientId;
import com.tfg.model.physiotherapist.Physiotherapist;
import com.tfg.application.port.out.persistence.PhysiotherapistRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IndibaSessionJpaRepositoryIT extends BaseRepositoryIT {

    @Autowired
    private IndibaJpaRepository indibaSessionJpaRepository;

    @Autowired
    private IndibaJpaDataRepository indibaSessionJpaDataRepository;

    @Autowired
    private PatientJpaRepository patientJpaRepository;

    @Autowired
    private PatientJpaDataRepository patientJpaDataRepository;

    @Autowired
    private PhysiotherapistRepository physiotherapistRepository;

    @AfterEach
    void tearDown() {
        indibaSessionJpaDataRepository.deleteAll();
        patientJpaDataRepository.deleteAll();
        physiotherapistRepository.deleteAll();
    }

    @Test
    public void givenSessionsForTwoPatients_whenFindAllByPatientId_thenReturnOnlyTargetPatientSessions() {
        Physiotherapist physio = PhysiotherapistFactory.createTestPsychiatrist("physio@test.com", "ValidPassword123!");
        physiotherapistRepository.save(physio);

        Patient patient1 = PatientFactory.createTestPatient("p1@test.com", "11111111A");
        Patient patient2 = PatientFactory.createTestPatient("p2@test.com", "22222222B");
        patientJpaRepository.save(patient1);
        patientJpaRepository.save(patient2);

        Date begin = new Date(System.currentTimeMillis() - 3600_000);
        Date end = new Date(System.currentTimeMillis());
        IndibaSession session1 = IndibaSessionFactory.createTestIndibaSession(patient1, physio, begin, end);
        IndibaSession session2 = IndibaSessionFactory.createTestIndibaSession(patient2, physio, begin, end);
        indibaSessionJpaRepository.save(session1);
        indibaSessionJpaRepository.save(session2);

        List<IndibaSession> result = indibaSessionJpaRepository.findAllByPatientId(
                new PatientId(patient1.getId().value())
        );

        assertEquals(1, result.size());
    }

    @Test
    public void givenNoSessionsForPatient_whenFindAllByPatientId_thenReturnEmptyList() {
        Patient patient = PatientFactory.createTestPatient("lonely@test.com", "33333333C");
        patientJpaRepository.save(patient);

        List<IndibaSession> result = indibaSessionJpaRepository.findAllByPatientId(
                new PatientId(patient.getId().value())
        );

        assertTrue(result.isEmpty());
    }
}
