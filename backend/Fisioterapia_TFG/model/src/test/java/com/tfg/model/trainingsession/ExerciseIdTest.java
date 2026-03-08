package com.tfg.model.trainingsession;

import com.tfg.trainingsession.ExerciseId;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class ExerciseIdTest {
    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0})
    void givenInvalidValue_newExerciseId_throwsException(int value) {
        ThrowableAssert.ThrowingCallable invocation = () -> new ExerciseId(value);

        assertThatIllegalArgumentException().isThrownBy(invocation);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 8_765, 2_000_000_000})
    void givenValidValue_newExerciseId_succeeds(int value) {
        ExerciseId exerciseId = new ExerciseId(value);

        assertThat(exerciseId.value()).isEqualTo(value);
    }
}
