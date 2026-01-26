package com.tfg.adapter.in.rest.common;

import com.tfg.patient.PatientId;

public class PatientIdParser {
    private PatientIdParser() {}

    public static PatientId parsePatientId(String string) {
        try {
            return new PatientId(Integer.parseInt(string));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
