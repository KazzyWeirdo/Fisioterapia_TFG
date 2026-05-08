package com.tfg.model.trainingsession;

import com.tfg.trainingsession.Exercise;
import com.tfg.trainingsession.ExerciseTemplate;

public class ExerciseTemplateFactory {

    public static final String DEFAULT_NAME = "Test Template";

    public static ExerciseTemplate createTestExerciseTemplate() {
        return new ExerciseTemplate(DEFAULT_NAME);
    }

    public static ExerciseTemplate createTestExerciseTemplate(String name) {
        return new ExerciseTemplate(name);
    }

    public static ExerciseTemplate createTestExerciseTemplateWithExercises(String name, Exercise... exercises) {
        ExerciseTemplate exerciseTemplate = new ExerciseTemplate(name);
        for (Exercise exercise : exercises) {
            exerciseTemplate.addExercise(exercise);
        }
        return exerciseTemplate;
    }
}
