package com.tfg.model.psychiatrist;

import com.tfg.psychiatrist.PsychiatristId;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class PsychiatristIdTest {

    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0})
    void givenInvalidValue_newPsychiatristId_throwsException(int value) {
        ThrowableAssert.ThrowingCallable invocation = () -> new PsychiatristId(value);

        assertThatIllegalArgumentException().isThrownBy(invocation);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 8_765, 2_000_000_000})
    void givenValidValue_newPsychiatristId_succeeds(int value) {
        PsychiatristId psychiatristId = new PsychiatristId(value);

        assertThat(psychiatristId.value()).isEqualTo(value);
    }
}
