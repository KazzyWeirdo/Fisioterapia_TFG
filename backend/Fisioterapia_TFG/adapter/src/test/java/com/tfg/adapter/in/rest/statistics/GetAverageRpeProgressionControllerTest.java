package com.tfg.adapter.in.rest.statistics;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.patient.Patient;
import com.tfg.application.port.in.statistics.GetAverageRpeProgressionUseCase;
import com.tfg.model.statistics.AverageRpeProgression;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GetAverageRpeProgressionControllerTest {
    @Mock
    private GetAverageRpeProgressionUseCase getAverageRpeProgressionUseCase;

    @InjectMocks
    private GetAverageRpeProgressionController getAverageRpeProgressionController;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final List<AverageRpeProgression> averageRpeProgressions = List.of(
            new AverageRpeProgression(LocalDate.of(2024, 3, 1), 6.5),
            new AverageRpeProgression(LocalDate.of(2024, 3, 8), 5.0)
    );

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getAverageRpeProgressionController)
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    public void givenValidPatientIdAndYear_whenGetPatientTransitionRatio_thenReturnsTransitionRatio() {
        given(getAverageRpeProgressionUseCase.calculateProgression(TEST_PATIENT.getId(), "Test_Exercise"))
                .willReturn(averageRpeProgressions);

        RestAssuredMockMvc.given()
                .when()
                .get("/statistics/{patientId}/{exerciseName}/workload-progression", String.valueOf(TEST_PATIENT.getId().value()), "Test_Exercise")
                .then()
                .statusCode(200);
    }

    @Test
    public void givenInvalidPatientIdAndYer_whenGetPatientTransitionRatio_thenReturnsBadRequest() {
        given(getAverageRpeProgressionUseCase.calculateProgression(null, "Test_Exercise"))
                .willThrow(new InvalidIdException());

        RestAssuredMockMvc.given()
                .when()
                .get("/statistics/{patientId}/{exerciseName}/workload-progression", "invalid-id", "Test_Exercise")
                .then()
                .statusCode(404);
    }

    @Test
    public void givenPatientWithNoTransitionRatio_whenGetPatientTransitionRatio_thenReturnsNoContent() {
        given(getAverageRpeProgressionUseCase.calculateProgression(TEST_PATIENT.getId(), "Test_Exercise"))
                .willReturn(List.of());

        RestAssuredMockMvc.given()
                .when()
                .get("/statistics/{patientId}/{exerciseName}/workload-progression", String.valueOf(TEST_PATIENT.getId().value()), "Test_Exercise")
                .then()
                .statusCode(204);
    }
}
