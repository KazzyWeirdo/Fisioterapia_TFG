package com.tfg.model.trainingsession;

import com.tfg.trainingsession.ExerciseSet;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExerciseSetTest {

    private static final ExerciseSet TEST_EXERCISE_SET = ExerciseSetFactory.createTestExerciseSet(1);

    @Test
    public void givenValidValues_newExerciseSet_succeeds() {
        ExerciseSet exerciseSet = TEST_EXERCISE_SET;

        assertThat(exerciseSet.reps()).isEqualTo(TEST_EXERCISE_SET.reps());
        assertThat(exerciseSet.rpe()).isEqualTo(TEST_EXERCISE_SET.rpe());
        assertThat(exerciseSet.setNumber()).isEqualTo(TEST_EXERCISE_SET.setNumber());
        assertThat(exerciseSet.restTimeSeconds()).isEqualTo(TEST_EXERCISE_SET.restTimeSeconds());
        assertThat(exerciseSet.weightKg()).isEqualTo(TEST_EXERCISE_SET.weightKg());
    }

    @Test
    public void givenInvalidSetNumber_throwsException() {
        try {
            new ExerciseSet(0, 10.0, 5, 30, 2);

        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Set number must be greater than 0");
        }
    }

    @Test
    public void givenInvalidWeight_throwsException() {
        try {
            new ExerciseSet(1, -10.0, 5, 30, 2);

        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Weight cannot be negative");
        }
    }

    @Test
    public void givenInvalidReps_throwsException() {
        try {
            new ExerciseSet(1, 10.0, -5, 30, 2);

        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Repetitions cannnot be negative");
        }
    }

    @Test
    public void givenInvalidRestTime_throwsException() {
        try {
            new ExerciseSet(1, 10.0, 5, -30, 2);

        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Rest time cannot be negative");
        }
    }

    @Test
    public void givenInvalidRpe_throwsException() {
        try {
            new ExerciseSet(1, 10.0, 5, 30, 20);

        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("RPE must be a value between 0 and 10");
        }
    }
}
