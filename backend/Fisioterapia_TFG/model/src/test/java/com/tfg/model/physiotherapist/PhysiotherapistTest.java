package com.tfg.model.physiotherapist;

import com.tfg.physiotherapist.Physiotherapist;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PhysiotherapistTest {

    private static final Physiotherapist TEST_PSYCHIATRIST = PhysiotherapistFactory.createTestPsychiatrist("hola@gmail.com", "ValidPass123!");

    @Test
    public void givenValidValues_newPsychiatrist_succeeds() {

        Physiotherapist psychiatrist = TEST_PSYCHIATRIST;

        assertThat(psychiatrist.getEmail()).isEqualTo(TEST_PSYCHIATRIST.getEmail());
        assertThat(psychiatrist.getPassword()).isEqualTo(TEST_PSYCHIATRIST.getPassword());
        assertThat(psychiatrist.getName()).isEqualTo(TEST_PSYCHIATRIST.getName());
        assertThat(psychiatrist.getSurname()).isEqualTo(TEST_PSYCHIATRIST.getSurname());
    }
}
