package com.tfg.model.psychiatrist;

import com.tfg.psychiatrist.PsychiatristEmail;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class PsychiatristEmailTest {

    @ParameterizedTest
    @ValueSource(strings = {"hola@gmail.com", "eduardo@edu.tecnocampus.cat", "swagger242@gmail.com"})
    public void givenValidValue_newPsychiatristEmail_succeeds(String value) {
        PsychiatristEmail psychiatristEmail = new PsychiatristEmail(value);

        assertThat(psychiatristEmail.value()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(strings = {"heyhola123", "señorarroba.com", "gmail.com"})
    public void givenInValidValue_newPsychiatristEmail_throwsException(String value) {
        ThrowableAssert.ThrowingCallable invocation = () -> new PsychiatristEmail(value);

        assertThatIllegalArgumentException().isThrownBy(invocation);
    }
}
