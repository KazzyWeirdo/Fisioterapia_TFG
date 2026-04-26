package com.tfg.adapter.in.rest.trainingsession;

public record TrainingSetExportWebModel(
        int patientId,
        int sessionId,
        String sessionDate,
        String exerciseName,
        int setNumber,
        double weightKg,
        int reps,
        int restTimeSeconds,
        int rpe
) {}
