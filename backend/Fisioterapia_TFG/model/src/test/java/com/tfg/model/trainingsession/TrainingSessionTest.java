package com.tfg.model.trainingsession;

import com.tfg.model.patient.PatientFactory;
import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import com.tfg.patient.Patient;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.trainingsession.ExerciseTemplate;
import com.tfg.trainingsession.TrainingSession;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TrainingSessionTest {

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final Physiotherapist TEST_PHYSIOTHERAPIST = PhysiotherapistFactory.createTestPsychiatrist("physio@test.com", "ValidPassword123!");
    private static final TrainingSession TEST_TRAINING_SESSION = TrainingSessionFactory.createTestTrainingSession(TEST_PATIENT, LocalDate.of(2025, 5, 20), TEST_PHYSIOTHERAPIST);

    @Test
    public void givenValidValues_newTrainingSession_succeeds() {
        TrainingSession trainingSession = TEST_TRAINING_SESSION;

        assertThat(trainingSession.getPatient()).isEqualTo(TEST_PATIENT);
        assertThat(trainingSession.getDate()).isEqualTo(LocalDate.of(2025, 5, 20));
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
}
