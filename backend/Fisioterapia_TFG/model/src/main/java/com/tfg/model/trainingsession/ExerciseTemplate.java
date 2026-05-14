package com.tfg.model.trainingsession;

<<<<<<<< HEAD:backend/Fisioterapia_TFG/model/src/main/java/com/tfg/trainingsession/TrainingSession.java
import com.tfg.patient.Patient;
import com.tfg.physiotherapist.Physiotherapist;
========
>>>>>>>> origin/develop:backend/Fisioterapia_TFG/model/src/main/java/com/tfg/model/trainingsession/ExerciseTemplate.java
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

<<<<<<<< HEAD:backend/Fisioterapia_TFG/model/src/main/java/com/tfg/trainingsession/TrainingSession.java
import java.time.LocalDateTime;
========
>>>>>>>> origin/develop:backend/Fisioterapia_TFG/model/src/main/java/com/tfg/model/trainingsession/ExerciseTemplate.java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@Getter
@Setter
<<<<<<<< HEAD:backend/Fisioterapia_TFG/model/src/main/java/com/tfg/trainingsession/TrainingSession.java
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
========
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
>>>>>>>> origin/develop:backend/Fisioterapia_TFG/model/src/main/java/com/tfg/model/trainingsession/ExerciseTemplate.java
    }

    public List<ExerciseTemplate> getExerciseTemplates() {
        return Collections.unmodifiableList(exerciseTemplates);
    }
}
