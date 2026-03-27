package com.tfg.model.psychiatrist;

import com.tfg.psychiatrist.Psychiatrist;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PsyhiatristTest {

    private static final Psychiatrist TEST_PSYCHIATRIST = PsychiatristFactory.createTestPsychiatrist("hola@gmail.com", "ValidPass123!");

    @Test
    public void givenValidValues_newPsychiatrist_succeeds() {

        Psychiatrist psychiatrist = TEST_PSYCHIATRIST;

        assertThat(psychiatrist.getEmail()).isEqualTo(TEST_PSYCHIATRIST.getEmail());
        assertThat(psychiatrist.getPassword()).isEqualTo(TEST_PSYCHIATRIST.getPassword());
        assertThat(psychiatrist.getName()).isEqualTo(TEST_PSYCHIATRIST.getName());
        assertThat(psychiatrist.getSurname()).isEqualTo(TEST_PSYCHIATRIST.getSurname());
    }
}
