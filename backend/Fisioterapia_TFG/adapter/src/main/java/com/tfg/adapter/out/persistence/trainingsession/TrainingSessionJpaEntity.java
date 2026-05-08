package com.tfg.adapter.out.persistence.trainingsession;

import com.tfg.adapter.out.persistence.exercisetemplate.ExerciseTemplateJpaEntity;
import com.tfg.adapter.out.persistence.patient.PatientJpaEntity;
import com.tfg.adapter.out.persistence.physiotherapist.PhysiotherapistJpaEntity;
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "physiotherapist_id", nullable = false)
    private PhysiotherapistJpaEntity physiotherapist;
    private java.time.LocalDateTime startDateTime;
    private java.time.LocalDateTime endDateTime;
    @OneToMany(mappedBy = "trainingSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseTemplateJpaEntity> exerciseTemplates = new ArrayList<>();

    public void addExerciseTemplate(ExerciseTemplateJpaEntity template) {
        exerciseTemplates.add(template);
        template.setTrainingSession(this);
    }
}
