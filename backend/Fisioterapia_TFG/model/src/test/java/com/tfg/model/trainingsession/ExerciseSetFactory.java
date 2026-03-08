package com.tfg.model.trainingsession;

import com.tfg.trainingsession.ExerciseSet;

public class ExerciseSetFactory {

    public static double WEIGHT_KG = 20.0;
    public static int REPS = 10;
    public static int REST_TIME_SECONDS = 60;
    public static int RPE = 7;

    public static ExerciseSet createTestExerciseSet(int setNumber) {
        return new ExerciseSet(
                setNumber,
                WEIGHT_KG,
                REPS,
                REST_TIME_SECONDS,
                RPE
        );
    }
}
