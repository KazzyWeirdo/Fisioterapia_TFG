package com.tfg.model.trainingsession;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@Getter
@Setter
public class ExerciseTemplate {
    private final ExerciseTemplateId id;
    private String name;
    private List<Exercise> exercises;

    public ExerciseTemplate(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Template name cannot be blank");
        this.id = new ExerciseTemplateId(ThreadLocalRandom.current().nextInt(1, 1_000_000));
        this.name = name;
        this.exercises = new ArrayList<>();
    }

    public void addExercise(Exercise exercise) {
        if (exercise == null) throw new IllegalArgumentException("Exercise cannot be null");
        this.exercises.add(exercise);
    }

    public List<Exercise> getExercises() {
        return Collections.unmodifiableList(exercises);
    }
}
