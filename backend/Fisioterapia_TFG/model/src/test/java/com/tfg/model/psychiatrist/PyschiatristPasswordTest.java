package com.tfg.model.psychiatrist;

import com.tfg.psychiatrist.PsychiatristPassword;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class PyschiatristPasswordTest {

    @ParameterizedTest
    @ValueSource(strings = {"ValidPass123!", "StrongPassword1@", "MySecurePass12#"})
    public void givenValidValue_newPsychiatristPassword_succeeds(String value) {
        PsychiatristPassword psychiatristPassword = new PsychiatristPassword(value);

        assertThat(psychiatristPassword.value()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(strings = {"short1!", "NoSpecialChar123", "nouppercase123!", "NOLOWERCASE123!", "NoDigitPass!"})
    public void givenInvalidValue_newPsychiatristPassword_throwsException(String value) {
        ThrowableAssert.ThrowingCallable invocation = () -> new PsychiatristPassword(value);

        assertThatIllegalArgumentException().isThrownBy(invocation);
    }
}
