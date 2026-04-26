package com.tfg.adapter.in.rest.trainingsession;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.trainingsession.ExerciseFactory;
import com.tfg.model.trainingsession.ExerciseSetFactory;
import com.tfg.model.trainingsession.TrainingSessionFactory;
import com.tfg.patient.Patient;
import com.tfg.port.in.trainingsession.GetAllTrainingSessionsForExportUseCase;
import com.tfg.trainingsession.Exercise;
import com.tfg.trainingsession.ExerciseSet;
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
import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GetAllTrainingSessionsForExportControllerTest {

    @Mock
    private GetAllTrainingSessionsForExportUseCase getAllTrainingSessionsForExportUseCase;

    @InjectMocks
    private GetAllTrainingSessionsForExportController getAllTrainingSessionsForExportController;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final ExerciseSet TEST_SET = ExerciseSetFactory.createTestExerciseSet(5);
    private static final Exercise TEST_EXERCISE = ExerciseFactory.createTestExerciseWithExerciseSets("Squat", TEST_SET);
    private static final TrainingSession TEST_SESSION = TrainingSessionFactory.createTestTrainingSessionWithExercises(
            TEST_PATIENT, LocalDate.of(2024, 1, 10), TEST_EXERCISE);

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getAllTrainingSessionsForExportController)
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    public void givenSessionsExist_whenExport_thenReturns200() {
        given(getAllTrainingSessionsForExportUseCase.getAllTrainingSessionsForExport()).willReturn(List.of(TEST_SESSION));

        RestAssuredMockMvc.when()
                .get("/training-session/export")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void givenNoSessions_whenExport_thenReturns204() {
        given(getAllTrainingSessionsForExportUseCase.getAllTrainingSessionsForExport()).willReturn(List.of());

        RestAssuredMockMvc.when()
                .get("/training-session/export")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
