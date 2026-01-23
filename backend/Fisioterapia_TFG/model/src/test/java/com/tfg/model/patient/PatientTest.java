package com.tfg.model.patient;

import com.tfg.patient.*;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class PatientTest {

    private static final PatientId PATIENT_ID = new PatientId(ThreadLocalRandom.current().nextInt(1_000_000));
    private static final PatientEmail PATIENT_EMAIL = new PatientEmail("hola@gmail.com");
    private static final PatientDNI PATIENT_DNI = new PatientDNI("12345678A");
    private static PatientGender PATIENT_GENDER = PatientGender.OTHER;
    private static String NAME = "Eduardo";
    private static String SURNAME = "Garcia";
    private static String SECOND_SURNAME = "Lopez";
    private static Date DATE_OF_BIRTH = new Date(2000, 5, 15);
    private static int PHONE_NUMBER = 123456789;

    @Test
    public void givenValidValues_newPatient_succeeds() {
        Patient patient = new Patient(
                PATIENT_ID,
                PATIENT_EMAIL,
                PATIENT_DNI,
                PATIENT_GENDER,
                NAME,
                SURNAME,
                SECOND_SURNAME,
                DATE_OF_BIRTH,
                PHONE_NUMBER
        );

        assertThat(patient.getId()).isEqualTo(PATIENT_ID);
        assertThat(patient.getEmail()).isEqualTo(PATIENT_EMAIL);
        assertThat(patient.getDni()).isEqualTo(PATIENT_DNI);
        assertThat(patient.getGender()).isEqualTo(PATIENT_GENDER);
        assertThat(patient.getName()).isEqualTo(NAME);
        assertThat(patient.getSurname()).isEqualTo(SURNAME);
        assertThat(patient.getSecondSurname()).isEqualTo(SECOND_SURNAME);
        assertThat(patient.getDateOfBirth()).isEqualTo(DATE_OF_BIRTH);
        assertThat(patient.getPhoneNumber()).isEqualTo(PHONE_NUMBER);
    }

}
