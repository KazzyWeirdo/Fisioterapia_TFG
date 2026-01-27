package com.tfg.adapter.in.rest.indiba;

import com.tfg.indiba.IndibaSession;
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
        @NotNull(message = "Intensity is required")
        float intensity,
        String objective,
        @NotNull(message = "Physiotherapist is required")
        String physiotherapist, // TODO: Change to PhysiotherapistId when the class is created
        String observations
) {
        public com.tfg.indiba.IndibaSession toDomainModel(){
                return new IndibaSession(
                        patientId,
                        beginSession,
                        endSession,
                        treatedArea,
                        com.tfg.indiba.IndibaSessionModes.valueOf(mode),
                        intensity,
                        objective,
                        physiotherapist,
                        observations
                );
        }
}
