package com.tfg.adapter.in.rest.pni;

public record PniExportWebModel(
        int patientId,
        int reportId,
        String reportDate,
        double hoursAsleep,
        double hrv,
        int ansCharge,
        int sleepScore
) {}
