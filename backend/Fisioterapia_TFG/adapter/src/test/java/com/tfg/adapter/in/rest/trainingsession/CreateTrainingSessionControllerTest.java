package com.tfg.adapter.in.rest.trainingsession;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import com.tfg.model.trainingsession.ExerciseTemplateFactory;
import com.tfg.patient.Patient;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.port.in.trainingsession.CreateTrainingSessionUseCase;
import com.tfg.port.out.persistence.ExerciseTemplateRepository;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.PhysiotherapistRepository;
import com.tfg.trainingsession.ExerciseTemplate;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateTrainingSessionControllerTest {

    @Mock
    private CreateTrainingSessionUseCase createTrainingSessionUseCase;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PhysiotherapistRepository physiotherapistRepository;

    @Mock
    private ExerciseTemplateRepository exerciseTemplateRepository;

    @InjectMocks
    private CreateTrainingSessionController createTrainingSessionController;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final Physiotherapist TEST_PHYSIO = PhysiotherapistFactory.createTestPsychiatrist("test@example.com", "password");
    private static final ExerciseTemplate TEST_TEMPLATE = ExerciseTemplateFactory.createTestExerciseTemplate();

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(createTrainingSessionController, new GlobalExceptionHandler());
    }

    @Test
    void createTrainingSession_shouldReturnOk_whenInputIsValid() {
        when(patientRepository.findById(any())).thenReturn(Optional.of(TEST_PATIENT));
        when(physiotherapistRepository.findById(any())).thenReturn(Optional.of(TEST_PHYSIO));
        when(exerciseTemplateRepository.findById(anyInt())).thenReturn(Optional.of(TEST_TEMPLATE));

        TrainingSessionCreationModel body = new TrainingSessionCreationModel(
                TEST_PATIENT.getId().value(),
                TEST_PHYSIO.getId().value(),
                LocalDateTime.of(2024, 6, 1, 10, 0),
                LocalDateTime.of(2024, 6, 1, 11, 0),
                TEST_TEMPLATE.getId().value(),
                null
        );

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/training-session/create")
                .then()
                .statusCode(200);
    }

    @Test
    void createTrainingSession_shouldReturnOk_whenExercisesAreProvided() {
        when(patientRepository.findById(any())).thenReturn(Optional.of(TEST_PATIENT));
        when(physiotherapistRepository.findById(any())).thenReturn(Optional.of(TEST_PHYSIO));
        when(exerciseTemplateRepository.findById(anyInt())).thenReturn(Optional.of(TEST_TEMPLATE));

        Map<String, Object> set = Map.of("setNumber", 1, "weightKg", 60.0, "reps", 10, "restTimeSeconds", 90, "rpe", 7);
        Map<String, Object> exercise = Map.of("name", "Sentadilla", "sets", List.of(set));
        Map<String, Object> body = new java.util.HashMap<>();
        body.put("patientId", TEST_PATIENT.getId().value());
        body.put("physiotherapistId", TEST_PHYSIO.getId().value());
        body.put("startDateTime", "2024-06-01T10:00:00");
        body.put("endDateTime", "2024-06-01T11:00:00");
        body.put("exerciseTemplateId", TEST_TEMPLATE.getId().value());
        body.put("exercises", List.of(exercise));

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/training-session/create")
                .then()
                .statusCode(200);
    }

    @Test
    void createTrainingSession_shouldReturnBadRequest_whenExerciseTemplateIdIsMissing() {
        Map<String, Object> body = Map.of(
                "patientId", TEST_PATIENT.getId().value(),
                "physiotherapistId", TEST_PHYSIO.getId().value(),
                "startDateTime", "2024-06-01T10:00:00",
                "endDateTime", "2024-06-01T11:00:00"
        );

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/training-session/create")
                .then()
                .statusCode(400);
    }

    @Test
    void createTrainingSession_shouldReturnBadRequest_whenPatientIdIsMissing() {
        Map<String, Object> body = Map.of(
                "physiotherapistId", TEST_PHYSIO.getId().value(),
                "startDateTime", "2024-06-01T10:00:00",
                "endDateTime", "2024-06-01T11:00:00",
                "exerciseTemplateId", 1
        );

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/training-session/create")
                .then()
                .statusCode(400);
    }

    @Test
    void createTrainingSession_shouldReturnBadRequest_whenPhysiotherapistIdIsMissing() {
        Map<String, Object> body = Map.of(
                "patientId", TEST_PATIENT.getId().value(),
                "startDateTime", "2024-06-01T10:00:00",
                "endDateTime", "2024-06-01T11:00:00",
                "exerciseTemplateId", 1
        );

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/training-session/create")
                .then()
                .statusCode(400);
    }

    @Test
    void createTrainingSession_shouldReturnBadRequest_whenEndDateTimeIsNotAfterStartDateTime() {
        when(patientRepository.findById(any())).thenReturn(Optional.of(TEST_PATIENT));
        when(physiotherapistRepository.findById(any())).thenReturn(Optional.of(TEST_PHYSIO));
        when(exerciseTemplateRepository.findById(anyInt())).thenReturn(Optional.of(TEST_TEMPLATE));

        Map<String, Object> body = Map.of(
                "patientId", TEST_PATIENT.getId().value(),
                "physiotherapistId", TEST_PHYSIO.getId().value(),
                "startDateTime", "2024-06-01T11:00:00",
                "endDateTime", "2024-06-01T10:00:00",
                "exerciseTemplateId", TEST_TEMPLATE.getId().value()
        );

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/training-session/create")
                .then()
                .statusCode(400);
    }
}
