package com.tfg.adapter.in.rest.trainingsession;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.trainingsession.TrainingSessionFactory;
import com.tfg.patient.Patient;
import com.tfg.port.in.trainingsession.GetTrainingSessionUseCase;
import com.tfg.trainingsession.TrainingSession;
import com.tfg.trainingsession.TrainingSessionId;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GetTrainingSessionControllerTest {

    @Mock
    private GetTrainingSessionUseCase getTrainingSessionUseCase;

    @InjectMocks
    private GetTrainingSessionController getTrainingSessionController;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final TrainingSession TEST_TRAINING_SESSION_1 = TrainingSessionFactory.createTestTrainingSession(TEST_PATIENT, LocalDateTime.of(2023, 11, 30, 10, 0), LocalDateTime.of(2023, 11, 30, 11, 0));

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getTrainingSessionController)
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    public void givenValidPatientId_whenGetTrainingSession_thenReturnsTrainingSession() {
        given(getTrainingSessionUseCase.getTrainingSession(TEST_TRAINING_SESSION_1.getId()))
                .willReturn(TEST_TRAINING_SESSION_1);

        RestAssuredMockMvc.given()
                .when()
                .get("/training-session/session/{sessionId}", String.valueOf(TEST_TRAINING_SESSION_1.getId().value()))
                .then()
                .statusCode(200);
    }

    @Test
    public void givenInvalidPatientId_whenGetTrainingSession_thenReturnsNotFound() {
        TrainingSessionId invalidSessionId = new TrainingSessionId(9999);
        given(getTrainingSessionUseCase.getTrainingSession(invalidSessionId))
                .willThrow(new InvalidIdException());

        RestAssuredMockMvc.given()
                .when()
                .get("/training-session/session/{sessionId}", String.valueOf(invalidSessionId.value()))
                .then()
                .statusCode(404);
    }
}
