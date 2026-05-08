package com.tfg.model.trainingsession;

import com.tfg.trainingsession.Exercise;
import com.tfg.trainingsession.ExerciseTemplate;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExerciseTemplateTest {

    private static final ExerciseTemplate TEST_TEMPLATE = ExerciseTemplateFactory.createTestExerciseTemplate();
    private static final Exercise TEST_EXERCISE = ExerciseFactory.createTestExercise("Squat");

    @Test
    public void givenValidValues_newExerciseTemplate_succeeds() {
        ExerciseTemplate template = TEST_TEMPLATE;

        assertThat(template.getName()).isEqualTo(ExerciseTemplateFactory.DEFAULT_NAME);
        assertThat(template.getId()).isNotNull();
    }

    @Test
    public void givenInvalidName_throwsException() {
        try {
            new ExerciseTemplate("");

        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Template name cannot be blank");
        }
    }

    @Test
    public void whenAddingExercise_exerciseIsAddedToTemplate() {
        TEST_TEMPLATE.addExercise(TEST_EXERCISE);

        assertThat(TEST_TEMPLATE.getExercises()).hasSize(1);
        assertThat(TEST_TEMPLATE.getExercises().get(0)).isEqualTo(TEST_EXERCISE);
    }

    @Test
    public void whenAddingExercise_exerciseIsNull_ThrowException() {
        try {
            TEST_TEMPLATE.addExercise(null);

        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Exercise cannot be null");
        }
    }
}
