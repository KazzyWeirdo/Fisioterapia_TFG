package com.tfg.model.patient;

import com.tfg.patient.*;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class PatientTest {

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");

    @Test
    public void givenValidValues_newPatient_succeeds() {

        Patient patient = TEST_PATIENT;

        assertThat(patient.getId()).isEqualTo(TEST_PATIENT.getId());
        assertThat(patient.getEmail()).isEqualTo(TEST_PATIENT.getEmail());
        assertThat(patient.getDni()).isEqualTo(TEST_PATIENT.getDni());
        assertThat(patient.getGender()).isEqualTo(TEST_PATIENT.getGender());
        assertThat(patient.getName()).isEqualTo(TEST_PATIENT.getName());
        assertThat(patient.getSurname()).isEqualTo(TEST_PATIENT.getSurname());
        assertThat(patient.getSecondSurname()).isEqualTo(TEST_PATIENT.getSecondSurname());
        assertThat(patient.getDateOfBirth()).isEqualTo(TEST_PATIENT.getDateOfBirth());
        assertThat(patient.getPhoneNumber()).isEqualTo(TEST_PATIENT.getPhoneNumber());
        assertThat(patient.getPolarAccessToken()).isEqualTo(TEST_PATIENT.getPolarAccessToken());
        assertThat(patient.getPolarUserId()).isEqualTo(TEST_PATIENT.getPolarUserId());
    }

}
