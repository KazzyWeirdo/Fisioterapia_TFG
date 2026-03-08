package com.tfg.model.trainingsession;

import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.trainingsession.Exercise;
import com.tfg.trainingsession.TrainingSession;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TrainingSessionTest {

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final TrainingSession TEST_TRAINING_SESSION = TrainingSessionFactory.createTestTrainingSession(TEST_PATIENT, LocalDate.of(2025, 5, 20));

    @Test
    public void givenValidValues_newTrainingSession_succeeds() {
        TrainingSession trainingSession = TEST_TRAINING_SESSION;

        assertThat(trainingSession.getPatient()).isEqualTo(TEST_PATIENT);
        assertThat(trainingSession.getDate()).isEqualTo(LocalDate.of(2025, 5, 20));
    }

     @Test
     public void whenAddingExercise_exerciseIsAddedToTrainingSession() {
         Exercise exercise = ExerciseFactory.createTestExercise("Test Exercise");
         TEST_TRAINING_SESSION.addExercise(exercise);

         assertThat(TEST_TRAINING_SESSION.getExercises()).hasSize(1);
         assertThat(TEST_TRAINING_SESSION.getExercises().get(0)).isEqualTo(exercise);
     }

     @Test
     public void whenAddingExercise_exerciseIsNull_ThrowException() {
         try {
             TEST_TRAINING_SESSION.addExercise(null);

         } catch (IllegalArgumentException e) {
             assertThat(e.getMessage()).isEqualTo("Exercise cannot be null");
         }
     }
}
