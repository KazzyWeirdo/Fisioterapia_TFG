package com.tfg.adapter.out.persistence.trainingsession;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ExerciseSetJpaEmbeddable {
    private int setNumber;
    private double weightKg;
    private int reps;
    private int restTimeSeconds;
    private int rpe;

    public ExerciseSetJpaEmbeddable() {}

    public ExerciseSetJpaEmbeddable(int setNumber, double weightKg, int reps, int restTimeSeconds, int rpe) {
        this.setNumber = setNumber;
        this.weightKg = weightKg;
        this.reps = reps;
        this.restTimeSeconds = restTimeSeconds;
        this.rpe = rpe;
    }
}
