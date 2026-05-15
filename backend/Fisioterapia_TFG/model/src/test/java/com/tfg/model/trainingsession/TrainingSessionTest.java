package com.tfg.model.trainingsession;

import com.tfg.model.patient.PatientFactory;
import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import com.tfg.model.patient.Patient;
import com.tfg.model.physiotherapist.Physiotherapist;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class TrainingSessionTest {

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final Physiotherapist TEST_PHYSIOTHERAPIST = PhysiotherapistFactory.createTestPsychiatrist("physio@test.com", "ValidPassword123!");
    private static final TrainingSession TEST_TRAINING_SESSION = TrainingSessionFactory.createTestTrainingSession(TEST_PATIENT, LocalDateTime.of(2025, 5, 20, 10, 0), LocalDateTime.of(2025, 5, 20, 11, 0), TEST_PHYSIOTHERAPIST);

    @Test
    public void givenValidValues_newTrainingSession_succeeds() {
        TrainingSession trainingSession = TEST_TRAINING_SESSION;

        assertThat(trainingSession.getPatient()).isEqualTo(TEST_PATIENT);
        assertThat(trainingSession.getStartDateTime()).isEqualTo(LocalDateTime.of(2025, 5, 20, 10, 0));
        assertThat(trainingSession.getEndDateTime()).isEqualTo(LocalDateTime.of(2025, 5, 20, 11, 0));
        assertThat(trainingSession.getPhysiotherapist()).isEqualTo(TEST_PHYSIOTHERAPIST);
    }

    @Test
    public void whenAddingExerciseTemplate_templateIsAddedToTrainingSession() {
        ExerciseTemplate template = ExerciseTemplateFactory.createTestExerciseTemplate();
        TEST_TRAINING_SESSION.addExerciseTemplate(template);

        assertThat(TEST_TRAINING_SESSION.getExerciseTemplates()).hasSize(1);
        assertThat(TEST_TRAINING_SESSION.getExerciseTemplates().get(0)).isEqualTo(template);
    }

    @Test
    public void whenAddingExerciseTemplate_templateIsNull_ThrowException() {
        try {
            TEST_TRAINING_SESSION.addExerciseTemplate(null);

        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("ExerciseTemplate cannot be null");
        }
    }

    @Test
    public void whenEndDateTimeIsNotAfterStartDateTime_ThrowException() {
        LocalDateTime startDateTime = LocalDateTime.of(2025, 5, 20, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2025, 5, 20, 10, 0);

        try {
            new TrainingSession(TEST_PATIENT, startDateTime, endDateTime, TEST_PHYSIOTHERAPIST);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("endDateTime must be after startDateTime");
        }
    }
}
