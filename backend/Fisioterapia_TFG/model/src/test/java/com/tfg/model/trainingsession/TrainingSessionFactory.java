package com.tfg.model.trainingsession;

import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import com.tfg.model.patient.Patient;
import com.tfg.model.physiotherapist.Physiotherapist;

import java.time.LocalDateTime;

public class TrainingSessionFactory {

    public static TrainingSession createTestTrainingSession(
            Patient patient,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    ) {
        Physiotherapist physiotherapist = PhysiotherapistFactory.createTestPsychiatrist("test@example.com", "password");
        return new TrainingSession(patient, startDateTime, endDateTime, physiotherapist);
    }

    public static TrainingSession createTestTrainingSession(
            Patient patient,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            Physiotherapist physiotherapist
    ) {
        return new TrainingSession(patient, startDateTime, endDateTime, physiotherapist);
    }

    public static TrainingSession createTestTrainingSessionWithExercises(
            Patient patient,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            Exercise... exercises
    ) {
        Physiotherapist physiotherapist = PhysiotherapistFactory.createTestPsychiatrist("test@example.com", "password");
        TrainingSession trainingSession = new TrainingSession(patient, startDateTime, endDateTime, physiotherapist);
        for (Exercise exercise : exercises) {
            trainingSession.addExerciseTemplate(new ExerciseTemplate("Default Template"));
            trainingSession.getExerciseTemplates().get(0).addExercise(exercise);
        }
        return trainingSession;
    }

    public static TrainingSession createTestTrainingSessionWithExerciseTemplates(
            Patient patient,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            ExerciseTemplate... exerciseTemplates
    ) {
        Physiotherapist physiotherapist = PhysiotherapistFactory.createTestPsychiatrist("test@example.com", "password");
        TrainingSession trainingSession = new TrainingSession(patient, startDateTime, endDateTime, physiotherapist);
        for (ExerciseTemplate exerciseTemplate : exerciseTemplates) {
            trainingSession.addExerciseTemplate(exerciseTemplate);
        }
        return trainingSession;
    }
}
