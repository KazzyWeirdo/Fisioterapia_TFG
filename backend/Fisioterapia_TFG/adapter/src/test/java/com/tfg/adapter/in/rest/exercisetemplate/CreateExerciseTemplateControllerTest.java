package com.tfg.adapter.in.rest.exercisetemplate;

import com.tfg.adapter.in.rest.trainingsession.ExerciseCreationModel;
import com.tfg.adapter.in.rest.trainingsession.ExerciseSetCreationModel;
import com.tfg.application.port.in.exercisetemplate.CreateExerciseTemplateUseCase;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class CreateExerciseTemplateControllerTest {

    @Mock
    private CreateExerciseTemplateUseCase createExerciseTemplateUseCase;

    @InjectMocks
    private CreateExerciseTemplateController createExerciseTemplateController;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(createExerciseTemplateController);
    }

    @Test
    void createExerciseTemplate_shouldReturnOk_whenInputIsValid() {
        ExerciseSetCreationModel set = new ExerciseSetCreationModel(1, 20.0, 10, 60, 7);
        ExerciseCreationModel exercise = new ExerciseCreationModel("Squat", List.of(set));
        ExerciseTemplateCreationModel body = new ExerciseTemplateCreationModel("Leg Day", List.of(exercise));

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/exercise-template")
                .then()
                .statusCode(200);
    }

    @Test
    void createExerciseTemplate_shouldReturnBadRequest_whenExercisesListIsEmpty() {
        ExerciseTemplateCreationModel body = new ExerciseTemplateCreationModel("Leg Day", new ArrayList<>());

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/exercise-template")
                .then()
                .statusCode(400);
    }

    @Test
    void createExerciseTemplate_shouldReturnBadRequest_whenNameIsMissing() {
        Map<String, Object> body = new java.util.HashMap<>();
        body.put("exercises", List.of(Map.of("name", "Squat", "exercises", List.of())));

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/exercise-template")
                .then()
                .statusCode(400);
    }
}
