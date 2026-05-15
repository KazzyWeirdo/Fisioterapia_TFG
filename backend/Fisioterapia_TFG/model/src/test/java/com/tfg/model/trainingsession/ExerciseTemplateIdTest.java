package com.tfg.model.trainingsession;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class ExerciseTemplateIdTest {

    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0})
    void givenInvalidValue_newExerciseTemplateId_throwsException(int value) {
        ThrowableAssert.ThrowingCallable invocation = () -> new ExerciseTemplateId(value);

        assertThatIllegalArgumentException().isThrownBy(invocation);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 500, 999_999})
    void givenValidValue_newExerciseTemplateId_succeeds(int value) {
        ExerciseTemplateId id = new ExerciseTemplateId(value);

        assertThat(id.value()).isEqualTo(value);
    }
}
