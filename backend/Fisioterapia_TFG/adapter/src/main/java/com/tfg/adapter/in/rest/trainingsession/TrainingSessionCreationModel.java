package com.tfg.adapter.in.rest.trainingsession;

import com.tfg.model.patient.Patient;
import com.tfg.model.patient.PatientId;
import com.tfg.model.physiotherapist.Physiotherapist;
import com.tfg.model.physiotherapist.PhysiotherapistId;
import com.tfg.application.port.out.persistence.ExerciseTemplateRepository;
import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.application.port.out.persistence.PhysiotherapistRepository;
import com.tfg.model.trainingsession.Exercise;
import com.tfg.model.trainingsession.ExerciseSet;
import com.tfg.model.trainingsession.ExerciseTemplate;
import com.tfg.model.trainingsession.TrainingSession;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record TrainingSessionCreationModel(
        @NotNull(message = "Patient ID is required") Integer patientId,
        @NotNull(message = "Physiotherapist ID is required") Integer physiotherapistId,
        @NotNull(message = "Start datetime is required") LocalDateTime startDateTime,
        @NotNull(message = "End datetime is required") LocalDateTime endDateTime,
        @NotNull(message = "Exercise template ID is required") Integer exerciseTemplateId,
        List<ExerciseCreationModel> exercises
) {
    public record ExerciseCreationModel(
            @NotBlank String name,
            @NotNull @NotEmpty List<ExerciseSetCreationModel> sets
    ) {}

    public record ExerciseSetCreationModel(
            int setNumber,
            double weightKg,
            int reps,
            int restTimeSeconds,
            int rpe
    ) {}

    public TrainingSession toDomainModel(PatientRepository patientRepository,
                                         PhysiotherapistRepository physiotherapistRepository,
                                         ExerciseTemplateRepository exerciseTemplateRepository) {
        Patient patient = patientRepository.findById(new PatientId(patientId))
                .orElseThrow(() -> new IllegalArgumentException("Patient with id " + patientId + " not found"));
        Physiotherapist physiotherapist = physiotherapistRepository.findById(new PhysiotherapistId(physiotherapistId))
                .orElseThrow(() -> new IllegalArgumentException("Physiotherapist with id " + physiotherapistId + " not found"));
        ExerciseTemplate source = exerciseTemplateRepository.findById(exerciseTemplateId)
                .orElseThrow(() -> new IllegalArgumentException("Exercise template with id " + exerciseTemplateId + " not found"));

        ExerciseTemplate sessionTemplate = new ExerciseTemplate(source.getName());

        if (exercises != null && !exercises.isEmpty()) {
            exercises.forEach(cmd -> {
                Exercise ex = new Exercise(cmd.name());
                cmd.sets().forEach(s -> ex.addSet(
                        new ExerciseSet(s.setNumber(), s.weightKg(), s.reps(), s.restTimeSeconds(), s.rpe())
                ));
                sessionTemplate.addExercise(ex);
            });
        } else {
            source.getExercises().forEach(ex -> {
                Exercise cloned = new Exercise(ex.getName());
                ex.getSets().forEach(s -> cloned.addSet(
                        new ExerciseSet(s.setNumber(), s.weightKg(), s.reps(), s.restTimeSeconds(), s.rpe())
                ));
                sessionTemplate.addExercise(cloned);
            });
        }

        TrainingSession trainingSession = new TrainingSession(patient, startDateTime, endDateTime, physiotherapist);
        trainingSession.addExerciseTemplate(sessionTemplate);
        return trainingSession;
    }
}
