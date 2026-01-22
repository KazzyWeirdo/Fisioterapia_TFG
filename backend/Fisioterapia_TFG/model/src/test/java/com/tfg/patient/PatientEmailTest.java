package com.tfg.patient;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class PatientEmailTest {

    @ParameterizedTest
    @ValueSource(strings = {"hola@gmail.com", "eduardo@edu.tecnocampus.cat", "swagger242@gmail.com"})
    public void givenValidValue_newPatientEmail_succeeds(String value) {
        PatientEmail patientEmail = new PatientEmail(value);

        assertThat(patientEmail.value()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(strings = {"heyhola123", "señorarroba.com", "gmail.com"})
    public void givenInValidValue_newPatientEmail_throwsException(String value) {
        ThrowableAssert.ThrowingCallable invocation = () -> new PatientEmail(value);

        assertThatIllegalArgumentException().isThrownBy(invocation);
    }
}
