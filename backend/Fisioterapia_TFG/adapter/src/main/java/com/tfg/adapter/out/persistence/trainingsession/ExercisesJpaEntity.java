package com.tfg.adapter.out.persistence.trainingsession;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class ExercisesJpaEntity {
    @Id
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_session_id")
    private TrainingSessionJpaEntity trainingSession;
    private String name;
    @ElementCollection
    @CollectionTable(
            name = "exercise_sets",
            joinColumns = @JoinColumn(name = "exercise_id")
    )
    private List<ExerciseSetJpaEmbeddable> sets = new ArrayList<>();

    public void addSet(ExerciseSetJpaEmbeddable set) {
        this.sets.add(set);
    }
}
