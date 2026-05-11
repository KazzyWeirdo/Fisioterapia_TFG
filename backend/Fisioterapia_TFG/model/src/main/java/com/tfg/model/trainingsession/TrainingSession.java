package com.tfg.model.trainingsession;

import com.tfg.model.patient.Patient;
import com.tfg.model.physiotherapist.Physiotherapist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private final Physiotherapist physiotherapist;
    private List<ExerciseTemplate> exerciseTemplates;

    public TrainingSession(Patient patient, LocalDateTime startDateTime, LocalDateTime endDateTime, Physiotherapist physiotherapist) {
        if (!endDateTime.isAfter(startDateTime)) {
            throw new IllegalArgumentException("endDateTime must be after startDateTime");
        }
        this.id = new TrainingSessionId(ThreadLocalRandom.current().nextInt(1_000_000));
        this.patient = patient;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
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
