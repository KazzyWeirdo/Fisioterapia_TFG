package com.tfg.adapter.in.rest.exercisetemplate;

import com.tfg.model.trainingsession.ExerciseTemplateFactory;
import com.tfg.port.in.exercisetemplate.GetAllExerciseTemplatesUseCase;
import com.tfg.trainingsession.ExerciseTemplate;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAllExerciseTemplatesControllerTest {

    @Mock
    private GetAllExerciseTemplatesUseCase getAllExerciseTemplatesUseCase;

    @InjectMocks
    private GetAllExerciseTemplatesController getAllExerciseTemplatesController;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(getAllExerciseTemplatesController);
    }

    @Test
    public void givenExistingTemplates_whenGetAll_thenReturnsOk() {
        List<ExerciseTemplate> templates = List.of(
                ExerciseTemplateFactory.createTestExerciseTemplate("Leg Day"),
                ExerciseTemplateFactory.createTestExerciseTemplate("Upper Body")
        );
        when(getAllExerciseTemplatesUseCase.getAllExerciseTemplates()).thenReturn(templates);

        RestAssuredMockMvc.given()
                .when()
                .get("/exercise-template")
                .then()
                .statusCode(200);
    }

    @Test
    public void givenNoTemplates_whenGetAll_thenReturnsEmptyList() {
        when(getAllExerciseTemplatesUseCase.getAllExerciseTemplates()).thenReturn(List.of());

        RestAssuredMockMvc.given()
                .when()
                .get("/exercise-template")
                .then()
                .statusCode(200);
    }
}
