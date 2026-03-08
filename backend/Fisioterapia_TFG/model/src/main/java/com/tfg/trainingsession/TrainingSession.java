package com.tfg.trainingsession;

import com.tfg.patient.Patient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@Getter
@Setter
public class TrainingSession {
    private final TrainingSessionId id;
    private final Patient patient;
    private LocalDate date;
    private List<Exercise> exercises;

    public TrainingSession(Patient patient, LocalDate date) {
        this.id = new TrainingSessionId(ThreadLocalRandom.current().nextInt(1_000_000));
        this.patient = patient;
        this.date = date;
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
