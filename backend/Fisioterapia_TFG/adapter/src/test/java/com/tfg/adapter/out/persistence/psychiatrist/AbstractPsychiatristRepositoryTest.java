package com.tfg.adapter.out.persistence.psychiatrist;


import com.tfg.model.psychiatrist.PsychiatristFactory;
import com.tfg.patient.Patient;
import com.tfg.port.out.persistence.PsychiatristRepository;
import com.tfg.psychiatrist.Psychiatrist;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractPsychiatristRepositoryTest {

    private Psychiatrist testPsychiatrist;

    @Autowired
    public PsychiatristRepository psychiatristRepository;

    @BeforeEach
    void setUp() {
        testPsychiatrist = PsychiatristFactory.createTestPsychiatrist("hola@gmail.com", "validPassword123!");
        psychiatristRepository.save(testPsychiatrist);
    }

    @AfterEach
    void tearDown() {
        psychiatristRepository.deleteAll();
    }

    @Test
    public void givenExistingEmail_whenFindByEmail_returnPsychiatrist() {
        var psychiatristOptional = psychiatristRepository.findByEmail(testPsychiatrist.getEmail());

        assertThat(psychiatristOptional).isPresent();
        assertThat(psychiatristOptional.get().getEmail()).isEqualTo(testPsychiatrist.getEmail());
    }

    @Test
    public void givenAnUnexistingEmail_whenFindByEmail_returnEmptyOptional() {
        Optional<Psychiatrist> optionalPatient = psychiatristRepository.findByEmail(testPsychiatrist.getEmail());

        assertThat(optionalPatient).isNotPresent();
    }

    @Test
    public void givenAnExistingId_whenFindById_returnPsychiatrist() {
        var psychiatristOptional = psychiatristRepository.findById(testPsychiatrist.getId());

        assertThat(psychiatristOptional).isPresent();
        assertThat(psychiatristOptional.get().getEmail()).isEqualTo(testPsychiatrist.getEmail());
    }

    @Test
    public void givenAnUnexistingId_whenFindById_returnEmptyOptional() {
        Optional<Psychiatrist> optionalPatient = psychiatristRepository.findById(testPsychiatrist.getId());

        assertThat(optionalPatient).isNotPresent();
    }

    @Test
    public void whenDeleteAll_thenNoPatientIsFound() {
        psychiatristRepository.deleteAll();
        var psychiatristOptional = psychiatristRepository.findById(testPsychiatrist.getId());

        assertThat(psychiatristOptional).isNotPresent();
    }

    @Test
    public void whenSave_thenPsychiatristIsSaved() {
        var psychiatristOptional = psychiatristRepository.findById(testPsychiatrist.getId());

        assertThat(psychiatristOptional).isPresent();
        assertThat(psychiatristOptional.get().getEmail()).isEqualTo(testPsychiatrist.getEmail());
    }
}
