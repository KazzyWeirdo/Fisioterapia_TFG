package com.tfg.adapter.in.rest.trainingsession;

import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.port.in.trainingsession.CreateTrainingSessionUseCase;
import com.tfg.port.out.persistence.PatientRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateTrainingSessionControllerTest {

    @Mock
    private CreateTrainingSessionUseCase createTrainingSessionUseCase;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private CreateTrainingSessionController createTrainingSessionController;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(createTrainingSessionController);
    }

    @Test
    void createTrainingSession_shouldReturnOk_whenInputIsValid() {
        when(patientRepository.findById(any()))
                .thenReturn(Optional.of(TEST_PATIENT));

        ExerciseCreationModel exercise1 = new ExerciseCreationModel("Squats", new ArrayList<>());

        List<ExerciseCreationModel>  exercises = new ArrayList<>();

        exercises.add(exercise1);

        TrainingSessionCreationModel trainingSessionCreationModel = new TrainingSessionCreationModel(
                LocalDate.of(2024, 6, 1),
                exercises
        );

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(trainingSessionCreationModel)
                .when()
                .post("/training-session/{patientId}/create", String.valueOf(TEST_PATIENT.getId().value()))
                .then()
                .statusCode(200);
    }

    @Test
    void createTrainingSession_shouldReturnBadRequest_whenInputIsInvalid() {
        TrainingSessionCreationModel trainingSessionCreationModel = new TrainingSessionCreationModel(
                LocalDate.of(2024, 6, 1),
                new ArrayList<>()
        );

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(trainingSessionCreationModel)
                .when()
                .post("/training-session/{patientId}/create", String.valueOf(TEST_PATIENT.getId().value()))
                .then()
                .statusCode(400);
    }
}
