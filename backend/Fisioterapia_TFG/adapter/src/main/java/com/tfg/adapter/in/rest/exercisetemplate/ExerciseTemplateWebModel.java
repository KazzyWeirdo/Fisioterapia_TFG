package com.tfg.adapter.in.rest.exercisetemplate;

import com.tfg.model.trainingsession.Exercise;
import com.tfg.model.trainingsession.ExerciseSet;
import com.tfg.model.trainingsession.ExerciseTemplate;

import java.util.List;

public record ExerciseTemplateWebModel(
        int id,
        String name,
        List<ExerciseWebModel> exercises
) {
    public static ExerciseTemplateWebModel from(ExerciseTemplate template) {
        return new ExerciseTemplateWebModel(
                template.getId().value(),
                template.getName(),
                template.getExercises().stream().map(ExerciseWebModel::from).toList()
        );
    }

    public record ExerciseWebModel(int id, String name, List<ExerciseSetWebModel> sets) {
        public static ExerciseWebModel from(Exercise ex) {
            return new ExerciseWebModel(
                    ex.getId().value(),
                    ex.getName(),
                    ex.getSets().stream().map(ExerciseSetWebModel::from).toList()
            );
        }
    }

    public record ExerciseSetWebModel(int setNumber, double weightKg, int reps, int restTimeSeconds, int rpe) {
        public static ExerciseSetWebModel from(ExerciseSet s) {
            return new ExerciseSetWebModel(s.setNumber(), s.weightKg(), s.reps(), s.restTimeSeconds(), s.rpe());
        }
    }
}
