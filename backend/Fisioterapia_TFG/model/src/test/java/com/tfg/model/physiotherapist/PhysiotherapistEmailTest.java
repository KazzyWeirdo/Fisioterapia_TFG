package com.tfg.model.physiotherapist;

import com.tfg.physiotherapist.PhysiotherapistEmail;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class PhysiotherapistEmailTest {

    @ParameterizedTest
    @ValueSource(strings = {"hola@gmail.com", "eduardo@edu.tecnocampus.cat", "swagger242@gmail.com"})
    public void givenValidValue_newPsychiatristEmail_succeeds(String value) {
        PhysiotherapistEmail psychiatristEmail = new PhysiotherapistEmail(value);

        assertThat(psychiatristEmail.value()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(strings = {"heyhola123", "señorarroba.com", "gmail.com"})
    public void givenInValidValue_newPsychiatristEmail_throwsException(String value) {
        ThrowableAssert.ThrowingCallable invocation = () -> new PhysiotherapistEmail(value);

        assertThatIllegalArgumentException().isThrownBy(invocation);
    }
}
