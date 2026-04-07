package com.tfg.adapter.in.rest.trainingsession;

import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.trainingsession.TrainingSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record TrainingSessionCreationModel(
        @NotNull(message = "Date is required")
        LocalDate date,
        @NotEmpty(message = "A session must have at least one exercise")
        @Valid
        List<ExerciseCreationModel> exercises
) {
        public TrainingSession toDomainModel(PatientRepository patientRepository, PatientId patientId) {
                Patient patient = patientRepository.findById(patientId)
                        .orElseThrow(() -> new IllegalArgumentException("Patient with id " + patientId + " not found"));

                TrainingSession trainingSession = new TrainingSession(patient, date);

                exercises.forEach(exerciseCreationModel -> trainingSession.addExercise(exerciseCreationModel.toDomainModel()));

                return trainingSession;
        }
}
