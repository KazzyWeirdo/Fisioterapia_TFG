package com.tfg.model.trainingsession;

import com.tfg.patient.Patient;
import com.tfg.trainingsession.Exercise;
import com.tfg.trainingsession.TrainingSession;

import java.time.LocalDate;

public class TrainingSessionFactory {

    public static TrainingSession createTestTrainingSession(
            Patient patient,
            LocalDate date
    ) {
        return new TrainingSession(patient, date);
    }

    public static TrainingSession createTestTrainingSessionWithExercises(
            Patient patient,
            LocalDate date,
            Exercise... exercises
    ) {
        TrainingSession trainingSession = new TrainingSession(patient, date);
        for (Exercise exercise : exercises) {
            trainingSession.addExercise(exercise);
        }
        return trainingSession;
    }
}
