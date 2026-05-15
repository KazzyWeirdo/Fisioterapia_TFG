package com.tfg.adapter.in.rest.patient;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientId;
import com.tfg.application.port.in.patient.UpdatePatientFunctionalScoreUseCase;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

@ExtendWith(MockitoExtension.class)
public class UpdatePatientFunctionalScoreControllerTest {

    @Mock
    private UpdatePatientFunctionalScoreUseCase updatePatientFunctionalScoreUseCase;

    @InjectMocks
    private UpdatePatientFunctionalScoreController updatePatientFunctionalScoreController;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(updatePatientFunctionalScoreController)
                        .setControllerAdvice(new com.tfg.adapter.in.rest.common.GlobalExceptionHandler())
        );
    }

    @Test
    public void updateFunctionalScore_ShouldReturnOk_WhenScoreIsValid() {
        given()
                .contentType("application/json")
                .body(Map.of("score", 75))
                .when()
                .patch("/patients/1/functional-score")
                .then()
                .status(HttpStatus.OK);

        verify(updatePatientFunctionalScoreUseCase).updateFunctionalScore(new PatientId(1), 75);
    }

    @Test
    public void updateFunctionalScore_ShouldReturnBadRequest_WhenScoreExceeds100() {
        given()
                .contentType("application/json")
                .body(Map.of("score", 150))
                .when()
                .patch("/patients/1/functional-score")
                .then()
                .status(HttpStatus.BAD_REQUEST);

        verify(updatePatientFunctionalScoreUseCase, never()).updateFunctionalScore(any(), anyInt());
    }

    @Test
    public void updateFunctionalScore_ShouldReturnBadRequest_WhenScoreIsNegative() {
        given()
                .contentType("application/json")
                .body(Map.of("score", -1))
                .when()
                .patch("/patients/1/functional-score")
                .then()
                .status(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void updateFunctionalScore_ShouldReturnNotFound_WhenPatientDoesNotExist() {
        doThrow(new InvalidIdException())
                .when(updatePatientFunctionalScoreUseCase)
                .updateFunctionalScore(any(PatientId.class), anyInt());

        given()
                .contentType("application/json")
                .body(Map.of("score", 75))
                .when()
                .patch("/patients/999/functional-score")
                .then()
                .status(HttpStatus.NOT_FOUND);
    }
}
