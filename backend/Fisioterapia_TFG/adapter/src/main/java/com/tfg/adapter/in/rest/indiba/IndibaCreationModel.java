package com.tfg.adapter.in.rest.indiba;

import com.tfg.indiba.IndibaSession;
import com.tfg.patient.Patient;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.PhysiotherapistRepository;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.util.Date;

public record IndibaCreationModel(
        @NotNull(message = "Patient is required")
        int patientId,
        @NotNull(message = "Begin session is required")
        @Past(message = "Begin session must be in the past")
        Date beginSession,
        @NotNull(message = "End session is required")
        Date endSession,
        @NotNull(message = "Treated area is required")
        String treatedArea,
        @NotNull(message = "Mode is required")
        String mode,
        Float capacitiveIntensity,
        Float resistiveIntensity,
        String objective,
        @NotNull(message = "Physiotherapist is required")
        int physiotherapistId,
        String observations
) {
        public com.tfg.indiba.IndibaSession toDomainModel(PatientRepository patientRepository, PhysiotherapistRepository physiotherapistRepository){
                Patient patient = patientRepository.findById(new com.tfg.patient.PatientId(patientId))
                        .orElseThrow(() -> new IllegalArgumentException("Patient with id " + patientId + " not found"));
                Physiotherapist physiotherapist = physiotherapistRepository.findById(new com.tfg.physiotherapist.PhysiotherapistId(physiotherapistId))
                        .orElseThrow(() -> new IllegalArgumentException("Physiotherapist with id " + physiotherapistId + " not found"));
                return new IndibaSession(
                        patient,
                        beginSession,
                        endSession,
                        treatedArea,
                        com.tfg.indiba.IndibaSessionModes.valueOf(mode),
                        capacitiveIntensity,
                        resistiveIntensity,
                        objective,
                        physiotherapist,
                        observations
                );
        }
}
