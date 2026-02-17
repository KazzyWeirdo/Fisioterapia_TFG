package com.tfg.model.patient;

import com.tfg.patient.*;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class PatientFactory {

    public static final PatientId PATIENT_ID = new PatientId(ThreadLocalRandom.current().nextInt(1_000_000));
    public static String PATIENT_GENDER = "OTHER";
    public static String NAME = "Eduardo";
    public static String SURNAME = "Garcia";
    public static String SECOND_SURNAME = "Lopez";
    public static LocalDate DATE_OF_BIRTH = LocalDate.of(1990, 5, 20);
    public static int PHONE_NUMBER = 123456789;

    public static Patient createTestPatient(String email, String dni) {
        Patient patient = new Patient(
                email,
                dni,
                PATIENT_GENDER,
                NAME,
                SURNAME,
                SECOND_SURNAME,
                DATE_OF_BIRTH,
                PHONE_NUMBER
        );
        return patient;
        }
}
