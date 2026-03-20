package com.tfg.adapter.in.rest.statistics;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.port.in.statistics.GetWorkloadProgressionUseCase;
import com.tfg.statistics.WorkloadProgression;
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
public class GetWorkloadProgressionControllerTest {
    @Mock
    private GetWorkloadProgressionUseCase getWorkloadProgressionUseCase;

    @InjectMocks
    private GetWorkloadProgressionController getWorkloadProgressionController;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final List<WorkloadProgression> workloadProgressions = List.of(
            new WorkloadProgression(LocalDate.of(2024, 3, 1), 3),
            new WorkloadProgression(LocalDate.of(2024, 3, 8), 4)
    );

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getWorkloadProgressionController)
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    public void givenValidPatientIdAndYear_whenGetPatientTransitionRatio_thenReturnsTransitionRatio() {
        given(getWorkloadProgressionUseCase.calculateProgression(TEST_PATIENT.getId(), "Test_Exercise"))
                .willReturn(workloadProgressions);

        RestAssuredMockMvc.given()
                .when()
                .get("/statistics/{patientId}/{exerciseName}/workload-progression", String.valueOf(TEST_PATIENT.getId().value()), "Test_Exercise")
                .then()
                .statusCode(200);
    }

    @Test
    public void givenInvalidPatientIdAndYer_whenGetPatientTransitionRatio_thenReturnsBadRequest() {
        given(getWorkloadProgressionUseCase.calculateProgression(null, "Test_Exercise"))
                .willThrow(new InvalidIdException());

        RestAssuredMockMvc.given()
                .when()
                .get("/statistics/{patientId}/{exerciseName}/workload-progression", "invalid-id", "Test_Exercise")
                .then()
                .statusCode(404);
    }

    @Test
    public void givenPatientWithNoTransitionRatio_whenGetPatientTransitionRatio_thenReturnsNoContent() {
        given(getWorkloadProgressionUseCase.calculateProgression(TEST_PATIENT.getId(), "Test_Exercise"))
                .willReturn(List.of());

        RestAssuredMockMvc.given()
                .when()
                .get("/statistics/{patientId}/{exerciseName}/workload-progression", String.valueOf(TEST_PATIENT.getId().value()), "Test_Exercise")
                .then()
                .statusCode(204);
    }
}
