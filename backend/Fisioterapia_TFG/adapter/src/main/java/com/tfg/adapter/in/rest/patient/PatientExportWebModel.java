package com.tfg.adapter.in.rest.patient;

public record PatientExportWebModel(
        int id,
        String dateOfBirth,
        String clinicalUseSex
) {}
