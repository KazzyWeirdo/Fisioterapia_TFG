package com.tfg.model.physiotherapist;

import com.tfg.physiotherapist.PhysiotherapistId;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class PhysiotherapistIdTest {

    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0})
    void givenInvalidValue_newPsychiatristId_throwsException(int value) {
        ThrowableAssert.ThrowingCallable invocation = () -> new PhysiotherapistId(value);

        assertThatIllegalArgumentException().isThrownBy(invocation);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 8_765, 2_000_000_000})
    void givenValidValue_newPsychiatristId_succeeds(int value) {
        PhysiotherapistId psychiatristId = new PhysiotherapistId(value);

        assertThat(psychiatristId.value()).isEqualTo(value);
    }
}
