package com.tfg.model.patient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.tfg.patient.PatientId;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class PatientIdTest {
    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0})
    void givenInvalidValue_newPatientId_throwsException(int value) {
        ThrowableAssert.ThrowingCallable invocation = () -> new PatientId(value);

        assertThatIllegalArgumentException().isThrownBy(invocation);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 8_765, 2_000_000_000})
    void givenValidValue_newPatientId_succeeds(int value) {
        PatientId patientId = new PatientId(value);

        assertThat(patientId.value()).isEqualTo(value);
    }
}
