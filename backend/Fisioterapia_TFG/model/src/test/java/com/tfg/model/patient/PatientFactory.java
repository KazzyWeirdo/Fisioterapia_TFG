package com.tfg.model.patient;

import com.tfg.patient.*;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class PatientFactory {

    public static final PatientId PATIENT_ID = new PatientId(ThreadLocalRandom.current().nextInt(1_000_000));
    public static PatientGender PATIENT_GENDER = PatientGender.OTHER;
    public static String NAME = "Eduardo";
    public static String SURNAME = "Garcia";
    public static String SECOND_SURNAME = "Lopez";
    public static Date DATE_OF_BIRTH = new Date(2000, 5, 15);
    public static int PHONE_NUMBER = 123456789;

    public static Patient createTestPatient(String email, String dni) {
        Patient patient = new Patient(
                PATIENT_ID,
                new PatientEmail(email),
                new PatientDNI(dni),
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
