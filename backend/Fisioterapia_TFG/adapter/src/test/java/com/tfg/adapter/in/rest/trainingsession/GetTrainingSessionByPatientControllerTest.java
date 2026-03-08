package com.tfg.adapter.in.rest.trainingsession;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.port.in.trainingsession.GetTrainingSessionByPatientUseCase;
import com.tfg.trainingsession.TrainingSession;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GetTrainingSessionByPatientControllerTest {

    @Mock
    private GetTrainingSessionByPatientUseCase getTrainingSessionByPatientUseCase;

    @InjectMocks
    private GetTrainingSessionByPatientController getTrainingSessionByPatientController;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final TrainingSession TEST_TRAINING_SESSION_1 = new TrainingSession(TEST_PATIENT, LocalDate.of(2023, 11, 30));
    private static final TrainingSession TEST_TRAINING_SESSION_2 = new TrainingSession(TEST_PATIENT, LocalDate.of(2023, 11, 29));

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getTrainingSessionByPatientController)
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    public void givenValidPatientId_whenGetTrainingSessionsFromPatient_thenReturnsSessions() {
        given(getTrainingSessionByPatientUseCase.getTrainingSessionFromPatient(TEST_PATIENT.getId()))
                .willReturn(List.of(TEST_TRAINING_SESSION_1.getDate(), TEST_TRAINING_SESSION_2.getDate()));

        RestAssuredMockMvc.given()
                .when()
                .get("/training-session/{patientId}", String.valueOf(TEST_PATIENT.getId().value()))
                .then()
                .statusCode(200);
    }

    @Test
    public void givenValidPatientId_whenGetTrainingSessionsFromPatient_thenReturnsEmptyList() {
        given(getTrainingSessionByPatientUseCase.getTrainingSessionFromPatient(TEST_PATIENT.getId()))
                .willReturn(Collections.emptyList());

        RestAssuredMockMvc.given()
                .when()
                .get("/training-session/{patientId}", String.valueOf(TEST_PATIENT.getId().value()))
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void givenInvalidPatientId_whenGetTrainingSessionsFromPatient_thenReturnsNotFound() {
        given(getTrainingSessionByPatientUseCase.getTrainingSessionFromPatient(TEST_PATIENT.getId()))
                .willThrow(new InvalidIdException());

        RestAssuredMockMvc.given()
                .when()
                .get("/training-session/{patientId}", String.valueOf(TEST_PATIENT.getId().value()))
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
