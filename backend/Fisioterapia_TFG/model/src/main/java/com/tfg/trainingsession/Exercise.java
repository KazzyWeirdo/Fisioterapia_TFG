package com.tfg.trainingsession;

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
public class Exercise {
    private final ExerciseId id;
    private String name;
    private List<ExerciseSet> sets;

    public Exercise(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name of the exercise cannot be blank");

        this.id = new ExerciseId(ThreadLocalRandom.current().nextInt(1_000_000));
        this.name = name;
        this.sets = new ArrayList<>();
    }

    public void addSet(ExerciseSet set) {
        if (set == null) throw new IllegalArgumentException("Set cannot be null");
        this.sets.add(set);
    }

    public List<ExerciseSet> getSets() {
        return Collections.unmodifiableList(sets);
    }
}
