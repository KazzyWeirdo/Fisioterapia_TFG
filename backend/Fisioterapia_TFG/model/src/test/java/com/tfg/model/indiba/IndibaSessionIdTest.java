package com.tfg.model.indiba;

import com.tfg.indiba.IndibaSessionId;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class IndibaSessionIdTest {
    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0})
    void givenInvalidValue_newIndibaSessionId_throwsException(int value) {
        ThrowableAssert.ThrowingCallable invocation = () -> new IndibaSessionId(value);

        assertThatIllegalArgumentException().isThrownBy(invocation);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 8_765, 2_000_000_000})
    void givenValidValue_newIndibaSessionId_succeeds(int value) {
        IndibaSessionId indibaSessionId = new IndibaSessionId(value);

        assertThat(indibaSessionId.value()).isEqualTo(value);
    }
}
