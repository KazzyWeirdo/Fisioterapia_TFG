package com.tfg.adapter.out.persistence.indiba;

import com.tfg.indiba.IndibaSession;
import com.tfg.model.indiba.IndibaSessionFactory;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.port.out.persistence.IndibaSessionRepository;
import com.tfg.port.out.persistence.PatientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractIndibaRepositoryTest {

    private Patient testPatient;
    private IndibaSession testIndibaSession;
    private IndibaSession testIndibaSession2;

    @Autowired
    public IndibaSessionRepository indibaSessionRepository;

    @Autowired
    public PatientRepository patientRepository;

    @BeforeEach
    void setUp() {
        testPatient = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");

        patientRepository.save(testPatient);

        testIndibaSession = new IndibaSessionFactory().createTestIndibaSession(
                testPatient,
                new Date(2023, 11, 30),
                new Date(2023, 12, 15)
        );

        testIndibaSession2 = new IndibaSessionFactory().createTestIndibaSession(
                testPatient,
                new Date(2023, 10, 30),
                new Date(2023, 11, 15)
        );
    }

    @AfterEach
    void tearDown() {
        indibaSessionRepository.deleteAll();
    }

    @Test
    public void givenExistingPatient_whenFindByPatient_returnIndibaSessions(){
        indibaSessionRepository.save(testIndibaSession);
        indibaSessionRepository.save(testIndibaSession2);

        var indibaSessions = indibaSessionRepository.findAllByPatientId(testPatient.getId());

        assertThat(indibaSessions).isNotEmpty();
        assertThat(indibaSessions.size()).isEqualTo(2);
    }

    @Test
    public void givenNonExistingPatient_whenFindByPatient_returnEmptyList(){
        var indibaSessions = indibaSessionRepository.findAllByPatientId(testPatient.getId());

        assertThat(indibaSessions).isEmpty();
    }

    @Test
    public void givenExistingId_whenFindById_returnIndibaSession(){
        indibaSessionRepository.save(testIndibaSession);

        var optionalIndibaSession = indibaSessionRepository.findById(testIndibaSession.getId());

        assertThat(optionalIndibaSession).isPresent();
        assertThat(optionalIndibaSession.get().getId()).isEqualTo(testIndibaSession.getId());
    }

    @Test
    public void givenNonExistingId_whenFindById_returnEmptyOptional(){
        var optionalIndibaSession = indibaSessionRepository.findById(testIndibaSession.getId());

        assertThat(optionalIndibaSession).isNotPresent();
    }

    @Test
    public void givenIndibaSession_whenSave_thenCanBeFound(){
        indibaSessionRepository.save(testIndibaSession);

        var optionalIndibaSession = indibaSessionRepository.findById(testIndibaSession.getId());

        assertThat(optionalIndibaSession).isPresent();
        assertThat(optionalIndibaSession.get().getId()).isEqualTo(testIndibaSession.getId());
    }

    @Test
    public void givenMultipleIndibaSessions_whenDeleteAll_thenRepositoryIsEmpty(){
        indibaSessionRepository.save(testIndibaSession);
        indibaSessionRepository.save(testIndibaSession2);

        indibaSessionRepository.deleteAll();

        var indibaSessions = indibaSessionRepository.findAllByPatientId(testPatient.getId());
        assertThat(indibaSessions).isEmpty();
    }
}
