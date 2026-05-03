package com.tfg.adapter.in.rest.indiba;

public record IndibaExportWebModel(
        int patientId,
        int sessionId,
        String beginSession,
        String endSession,
        String treatedArea,
        String mode,
        Float capacitiveIntensity,
        Float resistiveIntensity,
        String objective,
        String observations
) {}
