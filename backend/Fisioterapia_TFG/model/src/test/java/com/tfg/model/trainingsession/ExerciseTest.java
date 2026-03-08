package com.tfg.model.trainingsession;

import com.tfg.trainingsession.Exercise;
import com.tfg.trainingsession.ExerciseSet;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExerciseTest {

    private final static Exercise TEST_EXERCISE = new Exercise("Test Exercise");
    private final static ExerciseSet TEST_EXERCISE_TEST_SET = ExerciseSetFactory.createTestExerciseSet(1);

        @Test
        public void givenValidValues_newExercise_succeeds() {
            Exercise exercise = TEST_EXERCISE;

            assertThat(exercise.getName()).isEqualTo(TEST_EXERCISE.getName());
            assertThat(exercise.getId()).isEqualTo(TEST_EXERCISE.getId());
        }

        @Test
        public void givenInvalidName_throwsException() {
            try {
                new Exercise("");

            } catch (IllegalArgumentException e) {
                assertThat(e.getMessage()).isEqualTo("Name of the exercise cannot be blank");
            }
        }

        @Test
        public void whenAddingSet_setIsAddedToExercise() {
            Exercise exercise = TEST_EXERCISE;
            exercise.addSet(TEST_EXERCISE_TEST_SET);

            assertThat(exercise.getSets()).hasSize(1);
            assertThat(exercise.getSets().get(0)).isEqualTo(TEST_EXERCISE_TEST_SET);
        }

        @Test
        public void whenAddingSet_SetIsNull_ThrowException() {
            Exercise exercise = TEST_EXERCISE;

            try {
                exercise.addSet(null);

            } catch (IllegalArgumentException e) {
                assertThat(e.getMessage()).isEqualTo("Set cannot be null");
            }
        }
}
