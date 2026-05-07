package com.tfg.adapter.in.rest.trainingsession;

import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.physiotherapist.PhysiotherapistId;
import com.tfg.port.out.persistence.ExerciseTemplateRepository;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.PhysiotherapistRepository;
import com.tfg.trainingsession.ExerciseTemplate;
import com.tfg.trainingsession.TrainingSession;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TrainingSessionCreationModel(
        @NotNull(message = "Patient ID is required")
        Integer patientId,
        @NotNull(message = "Physiotherapist ID is required")
        Integer physiotherapistId,
        @NotNull(message = "Start datetime is required")
        LocalDateTime startDateTime,
        @NotNull(message = "End datetime is required")
        LocalDateTime endDateTime,
        @NotNull(message = "Exercise template ID is required")
        Integer exerciseTemplateId
) {
        public TrainingSession toDomainModel(PatientRepository patientRepository,
                                             PhysiotherapistRepository physiotherapistRepository,
                                             ExerciseTemplateRepository exerciseTemplateRepository) {
                Patient patient = patientRepository.findById(new PatientId(patientId))
                        .orElseThrow(() -> new IllegalArgumentException("Patient with id " + patientId + " not found"));
                Physiotherapist physiotherapist = physiotherapistRepository.findById(new PhysiotherapistId(physiotherapistId))
                        .orElseThrow(() -> new IllegalArgumentException("Physiotherapist with id " + physiotherapistId + " not found"));
                ExerciseTemplate template = exerciseTemplateRepository.findById(exerciseTemplateId)
                        .orElseThrow(() -> new IllegalArgumentException("Exercise template with id " + exerciseTemplateId + " not found"));

                TrainingSession trainingSession = new TrainingSession(patient, startDateTime, endDateTime, physiotherapist);
                trainingSession.addExerciseTemplate(template);

                return trainingSession;
        }
}
