package com.tfg.trainingsession;

import com.tfg.patient.Patient;
import com.tfg.physiotherapist.Physiotherapist;
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
    private final Physiotherapist physiotherapist;
    private List<ExerciseTemplate> exerciseTemplates;

    public TrainingSession(Patient patient, LocalDate date, Physiotherapist physiotherapist) {
        this.id = new TrainingSessionId(ThreadLocalRandom.current().nextInt(1_000_000));
        this.patient = patient;
        this.date = date;
        this.physiotherapist = physiotherapist;
        this.exerciseTemplates = new ArrayList<>();
    }


    public void addExerciseTemplate(ExerciseTemplate exerciseTemplate) {
        if (exerciseTemplate == null) throw new IllegalArgumentException("ExerciseTemplate cannot be null");
        this.exerciseTemplates.add(exerciseTemplate);
    }

    public List<ExerciseTemplate> getExerciseTemplates() {
        return Collections.unmodifiableList(exerciseTemplates);
    }
}
