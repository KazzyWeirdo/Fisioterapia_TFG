package com.tfg.adapter.in.rest.statistics;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.port.in.statistics.GetPatientTransitionRatioUseCase;
import com.tfg.statistics.PatientMonthTransitionRatio;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GetPatientTransitionRatioControllerTest {
    @Mock
    private GetPatientTransitionRatioUseCase getPatientTransitionRatioUseCase;

     @InjectMocks
     private GetPatientTransitionRatioController getPatientTransitionRatioController;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final List<PatientMonthTransitionRatio> sessionCounts = List.of(
            new PatientMonthTransitionRatio(1, 2024, 3, 5),
            new PatientMonthTransitionRatio(2, 2024, 4, 0)
    );

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getPatientTransitionRatioController)
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    public void givenValidPatientIdAndYear_whenGetPatientTransitionRatio_thenReturnsTransitionRatio() {
        given(getPatientTransitionRatioUseCase.getTransitionRatio(TEST_PATIENT.getId(), 2023))
                .willReturn(sessionCounts);

        RestAssuredMockMvc.given()
                .when()
                .get("/statistics/{patientId}/{year}/patient-transition-ratio", String.valueOf(TEST_PATIENT.getId().value()), String.valueOf(2023))
                .then()
                .statusCode(200);
    }

    @Test
    public void givenInvalidPatientIdAndYer_whenGetPatientTransitionRatio_thenReturnsBadRequest() {
        given(getPatientTransitionRatioUseCase.getTransitionRatio(null, 2023))
                .willThrow(new InvalidIdException());

        RestAssuredMockMvc.given()
                .when()
                .get("/statistics/{patientId}/{year}/patient-transition-ratio", "invalid-id", String.valueOf(2023))
                .then()
                .statusCode(404);
    }

    @Test
    public void givenPatientWithNoTransitionRatio_whenGetPatientTransitionRatio_thenReturnsNoContent() {
        given(getPatientTransitionRatioUseCase.getTransitionRatio(TEST_PATIENT.getId(), 2023))
                .willReturn(List.of());

        RestAssuredMockMvc.given()
                .when()
                .get("/statistics/{patientId}/{year}/patient-transition-ratio", String.valueOf(TEST_PATIENT.getId().value()), String.valueOf(2023))
                .then()
                .statusCode(204);
    }
}
