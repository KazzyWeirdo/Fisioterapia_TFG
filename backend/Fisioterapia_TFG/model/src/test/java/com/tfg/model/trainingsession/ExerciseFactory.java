package com.tfg.model.trainingsession;

public class ExerciseFactory {

    public static Exercise createTestExercise(String name) {
        return new Exercise(name);
    }

    public static Exercise createTestExerciseWithExerciseSets(String name, ExerciseSet... sets) {
        Exercise exercise = new Exercise(name);
        for (int i = 0; i < sets.length; i++) {
            exercise.addSet(sets[i]);
        }
        return exercise;

    }
}
