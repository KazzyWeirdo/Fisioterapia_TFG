package com.tfg.model.trainingsession;

import com.tfg.trainingsession.Exercise;

public class ExerciseFactory {

    public static Exercise createTestExercise(String name) {
        return new Exercise(name);
    }
}
