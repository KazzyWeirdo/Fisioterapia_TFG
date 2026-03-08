package com.tfg.model.trainingsession;

import com.tfg.trainingsession.TrainingSessionId;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class TrainingSessionIdTest {

    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0})
    void givenInvalidValue_newTrainingSessionId_throwsException(int value) {
        ThrowableAssert.ThrowingCallable invocation = () -> new TrainingSessionId(value);

        assertThatIllegalArgumentException().isThrownBy(invocation);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 8_765, 2_000_000_000})
    void givenValidValue_newTrainingSessionId_succeeds(int value) {
        TrainingSessionId trainingSessionId = new TrainingSessionId(value);

        assertThat(trainingSessionId.value()).isEqualTo(value);
    }
}
