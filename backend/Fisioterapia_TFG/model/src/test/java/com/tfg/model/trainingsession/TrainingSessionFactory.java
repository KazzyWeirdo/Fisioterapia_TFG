package com.tfg.model.trainingsession;

import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import com.tfg.patient.Patient;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.trainingsession.Exercise;
import com.tfg.trainingsession.ExerciseTemplate;
import com.tfg.trainingsession.TrainingSession;

import java.time.LocalDate;

public class TrainingSessionFactory {

    public static TrainingSession createTestTrainingSession(
            Patient patient,
            LocalDate date
    ) {
        Physiotherapist physiotherapist = PhysiotherapistFactory.createTestPsychiatrist("test@example.com", "password");
        return new TrainingSession(patient, date, physiotherapist);
    }

    public static TrainingSession createTestTrainingSession(
            Patient patient,
            LocalDate date,
            Physiotherapist physiotherapist
    ) {
        return new TrainingSession(patient, date, physiotherapist);
    }

    public static TrainingSession createTestTrainingSessionWithExercises(
            Patient patient,
            LocalDate date,
            Exercise... exercises
    ) {
        Physiotherapist physiotherapist = PhysiotherapistFactory.createTestPsychiatrist("test@example.com", "password");
        TrainingSession trainingSession = new TrainingSession(patient, date, physiotherapist);
        for (Exercise exercise : exercises) {
            trainingSession.addExerciseTemplate(new ExerciseTemplate("Default Template"));
            trainingSession.getExerciseTemplates().get(0).addExercise(exercise);
        }
        return trainingSession;
    }

    public static TrainingSession createTestTrainingSessionWithExerciseTemplates(
            Patient patient,
            LocalDate date,
            ExerciseTemplate... exerciseTemplates
    ) {
        Physiotherapist physiotherapist = PhysiotherapistFactory.createTestPsychiatrist("test@example.com", "password");
        TrainingSession trainingSession = new TrainingSession(patient, date, physiotherapist);
        for (ExerciseTemplate exerciseTemplate : exerciseTemplates) {
            trainingSession.addExerciseTemplate(exerciseTemplate);
        }
        return trainingSession;
    }
}
