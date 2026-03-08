package com.tfg.adapter.out.persistence.trainingsession;

import com.tfg.adapter.out.persistence.patient.PatientJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "training_sessions")
public class TrainingSessionJpaEntity {
    @Id
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientJpaEntity patient;
    @Temporal(TemporalType.DATE)
    private java.time.LocalDate date;
    @OneToMany(mappedBy = "trainingSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExercisesJpaEntity> exercises = new ArrayList<>();

    public void addExercise(ExercisesJpaEntity exercise) {
        exercises.add(exercise);
        exercise.setTrainingSession(this);
    }
}
