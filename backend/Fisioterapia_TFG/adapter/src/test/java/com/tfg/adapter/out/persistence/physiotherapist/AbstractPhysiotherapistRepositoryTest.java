package com.tfg.adapter.out.persistence.physiotherapist;


import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import com.tfg.port.out.persistence.PhysiotherapistRepository;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.physiotherapist.PhysiotherapistEmail;
import com.tfg.physiotherapist.PhysiotherapistId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractPhysiotherapistRepositoryTest {

    private Physiotherapist testPsychiatrist;

    @Autowired
    public PhysiotherapistRepository psychiatristRepository;

    @BeforeEach
    void setUp() {
        testPsychiatrist = PhysiotherapistFactory.createTestPsychiatrist("hola@gmail.com", "validPassword123!");
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
        PhysiotherapistEmail unexistingEmail = new PhysiotherapistEmail("invalid@gmail.com");
        Optional<Physiotherapist> optionalPsychiatrist = psychiatristRepository.findByEmail(unexistingEmail);

        assertThat(optionalPsychiatrist).isNotPresent();
    }

    @Test
    public void givenAnExistingId_whenFindById_returnPsychiatrist() {
        var psychiatristOptional = psychiatristRepository.findById(testPsychiatrist.getId());

        assertThat(psychiatristOptional).isPresent();
        assertThat(psychiatristOptional.get().getEmail()).isEqualTo(testPsychiatrist.getEmail());
    }

    @Test
    public void givenAnUnexistingId_whenFindById_returnEmptyOptional() {
        PhysiotherapistId unexistingId = new PhysiotherapistId(999);
        Optional<Physiotherapist> optionalPsychiatrist = psychiatristRepository.findById(unexistingId);

        assertThat(optionalPsychiatrist).isNotPresent();
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
