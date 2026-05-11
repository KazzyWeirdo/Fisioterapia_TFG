package com.tfg.adapter.in.rest.indiba;

import com.tfg.model.indiba.IndibaSession;
import com.tfg.model.indiba.IndibaSessionModes;
import com.tfg.model.patient.PatientId;
import com.tfg.model.patient.Patient;
import com.tfg.model.physiotherapist.Physiotherapist;
import com.tfg.model.physiotherapist.PhysiotherapistId;
import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.application.port.out.persistence.PhysiotherapistRepository;
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
        @NotNull(message = "Physiotherapist is required")
        int physiotherapistId,
        String observations
) {
        public IndibaSession toDomainModel(PatientRepository patientRepository, PhysiotherapistRepository physiotherapistRepository){
                Patient patient = patientRepository.findById(new PatientId(patientId))
                        .orElseThrow(() -> new IllegalArgumentException("Patient with id " + patientId + " not found"));
                Physiotherapist physiotherapist = physiotherapistRepository.findById(new PhysiotherapistId(physiotherapistId))
                        .orElseThrow(() -> new IllegalArgumentException("Physiotherapist with id " + physiotherapistId + " not found"));
                return new IndibaSession(
                        patient,
                        beginSession,
                        endSession,
                        treatedArea,
                        IndibaSessionModes.valueOf(mode),
                        capacitiveIntensity,
                        resistiveIntensity,
                        physiotherapist,
                        observations
                );
        }
}
