package com.tfg.adapter.out.persistence.exercisetemplate;

import com.tfg.adapter.out.persistence.trainingsession.ExercisesJpaEntity;
import com.tfg.adapter.out.persistence.trainingsession.TrainingSessionJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "exercise_templates")
public class ExerciseTemplateJpaEntity {
    @Id
    private int id;
    private String name;
    @OneToMany(mappedBy = "exerciseTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExercisesJpaEntity> exercises = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_session_id")
    private TrainingSessionJpaEntity trainingSession;

    public void addExercise(ExercisesJpaEntity exercise) {
        this.exercises.add(exercise);
        exercise.setExerciseTemplate(this);
    }
}
