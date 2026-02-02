package com.tfg.adapter.in.rest.indiba;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.exceptions.InvalidIdException;
import com.tfg.indiba.IndibaSession;
import com.tfg.model.indiba.IndibaSessionFactory;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.port.in.indiba.GetIndibaSessionFromPatientUseCase;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GetIndibaSessionFromPatientControllerTest {

    @Mock
    private GetIndibaSessionFromPatientUseCase getIndibaSessionFromPatientUseCase;

    @InjectMocks
    private GetIndibaSessionFromPatientController getIndibaSessionFromPatientController;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final IndibaSession TEST_INDIBA_SESSION_1 = new IndibaSessionFactory().createTestIndibaSession(TEST_PATIENT, new Date(2023, 11, 30), new Date(2023, 12, 15));
    private static final IndibaSession TEST_INDIBA_SESSION_2 = new IndibaSessionFactory().createTestIndibaSession(TEST_PATIENT, new Date(2023, 12, 16), new Date(2023, 12, 20));

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getIndibaSessionFromPatientController)
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    public void givenValidPatientId_whenGetIndibaSessionsFromPatient_thenReturnsSessions() {
        given(getIndibaSessionFromPatientUseCase.getIndibaSessionsFromPatient(TEST_PATIENT.getId()))
                .willReturn(List.of(TEST_INDIBA_SESSION_1.getBeginSession(), TEST_INDIBA_SESSION_2.getBeginSession()));

        RestAssuredMockMvc.given()
                .when()
                .get("/indiba/{patientId}", String.valueOf(TEST_PATIENT.getId().value()))
                .then()
                .statusCode(HttpStatus.OK.value());
    }


    @Test
    public void givenValidPatientId_whenGetIndibaSessionsFromPatient_thenReturnsNoContent() {
        given(getIndibaSessionFromPatientUseCase.getIndibaSessionsFromPatient(TEST_PATIENT.getId()))
                .willReturn(Collections.emptyList());

        RestAssuredMockMvc.given()
                .when()
                .get("/indiba/{patientId}", String.valueOf(TEST_PATIENT.getId().value()))
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void givenInvalidPatientId_whenGetIndibaSessionsFromPatient_thenReturnsNotFound() {
        given(getIndibaSessionFromPatientUseCase.getIndibaSessionsFromPatient(TEST_PATIENT.getId()))
                .willThrow(new InvalidIdException());

        RestAssuredMockMvc.given()
                .when()
                .get("/indiba/{patientId}", String.valueOf(TEST_PATIENT.getId().value()))
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
