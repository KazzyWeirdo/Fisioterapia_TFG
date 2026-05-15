package com.tfg.adapter.in.rest.exercisetemplate;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.application.port.in.exercisetemplate.GetExerciseTemplateByIdUseCase;
import com.tfg.model.trainingsession.ExerciseTemplateFactory;
import com.tfg.model.trainingsession.ExerciseTemplate;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GetExerciseTemplateByIdControllerTest {

    @Mock
    private GetExerciseTemplateByIdUseCase getExerciseTemplateByIdUseCase;

    @InjectMocks
    private GetExerciseTemplateByIdController getExerciseTemplateByIdController;

    private static final ExerciseTemplate TEST_TEMPLATE = ExerciseTemplateFactory.createTestExerciseTemplate("Leg Day");

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getExerciseTemplateByIdController)
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    void givenExistingId_getById_returns200WithTemplate() {
        given(getExerciseTemplateByIdUseCase.getById(1)).willReturn(Optional.of(TEST_TEMPLATE));

        RestAssuredMockMvc.given()
                .when()
                .get("/exercise-template/{id}", 1)
                .then()
                .statusCode(200);
    }

    @Test
    void givenNonExistingId_getById_returns404() {
        given(getExerciseTemplateByIdUseCase.getById(999)).willReturn(Optional.empty());

        RestAssuredMockMvc.given()
                .when()
                .get("/exercise-template/{id}", 999)
                .then()
                .statusCode(404);
    }
}
