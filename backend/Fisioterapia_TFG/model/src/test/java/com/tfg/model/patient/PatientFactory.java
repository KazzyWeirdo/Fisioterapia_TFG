package com.tfg.model.patient;

import com.tfg.patient.*;

import java.time.LocalDate;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class PatientFactory {

    public static final PatientId PATIENT_ID = new PatientId(ThreadLocalRandom.current().nextInt(1_000_000));
    public static String PATIENT_GENDER = "OTHER";
    public static String PATIENT_ADMIN_SEX = "UNKNOWN";
    public static String PATIENT_CLINICAL_SEX = "UNKNOWN";
    public static String LEGAL_NAME = "Eduardo";
    public static String NAME_TO_USE = "Barna";
    public static String SURNAME = "Garcia";
    public static String SECOND_SURNAME = "Lopez";
    public static String PRONOUNS = "they/them";
    public static LocalDate DATE_OF_BIRTH = LocalDate.of(1990, 5, 20);
    public static int PHONE_NUMBER = 123456789;

    public static Patient createTestPatient(String email, String dni) {
        Patient patient = new Patient(
                email,
                dni,
                PATIENT_GENDER,
                PATIENT_CLINICAL_SEX,
                PATIENT_ADMIN_SEX,
                LEGAL_NAME,
                NAME_TO_USE,
                SURNAME,
                SECOND_SURNAME,
                PRONOUNS,
                DATE_OF_BIRTH,
                PHONE_NUMBER
        );
        return patient;
        }
}
