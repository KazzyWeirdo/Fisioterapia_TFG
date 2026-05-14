package com.tfg.model.patient;

import com.tfg.patient.PatientDNI;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class PatientDNITest {

    @ParameterizedTest
    @ValueSource(strings = {"50367892K", "11111111L", "12345678M"})
    public void givenValidValue_newPatientDNI_succeeds(String value) {
        PatientDNI patientDNI = new PatientDNI(value);

        assertThat(patientDNI.value()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(strings = {"124567546", "K12384950", "KLDJVMJK3"})
    public void givenInValidValue_newPatientDNI_throwsException(String value) {
        ThrowableAssert.ThrowingCallable invocation = () -> new PatientDNI(value);

        assertThatIllegalArgumentException().isThrownBy(invocation);
    }
}
