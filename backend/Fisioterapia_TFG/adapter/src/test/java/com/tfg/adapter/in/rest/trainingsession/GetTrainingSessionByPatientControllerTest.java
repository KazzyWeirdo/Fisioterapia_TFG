package com.tfg.adapter.in.rest.trainingsession;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.TrainingSessionSummaryElement;
import com.tfg.port.in.trainingsession.GetTrainingSessionByPatientUseCase;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GetTrainingSessionByPatientControllerTest {

    @Mock
    private GetTrainingSessionByPatientUseCase getTrainingSessionByPatientUseCase;

    @InjectMocks
    private GetTrainingSessionByPatientController getTrainingSessionByPatientController;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final TrainingSessionSummaryElement TEST_TRAINING_SESSION_1 = new TrainingSessionSummaryElement(1, LocalDate.of(2023, 12, 15));

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getTrainingSessionByPatientController)
                        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    public void givenValidPatientId_whenGetTrainingSessionsFromPatient_thenReturnsSessions() {
        PagedResponse<TrainingSessionSummaryElement> pagedResponse = new PagedResponse<>(
                List.of(TEST_TRAINING_SESSION_1), 1, 1, 0, true
        );

        given(getTrainingSessionByPatientUseCase.getTrainingSessionFromPatient(any(PageQuery.class),eq(TEST_PATIENT.getId())))
                .willReturn(pagedResponse);

        RestAssuredMockMvc.given()
                .param("page", "0")
                .param("size", "10")
                .when()
                .get("/training-session/{patientId}", String.valueOf(TEST_PATIENT.getId().value()))
                .then()
                .statusCode(200);
    }

    @Test
    public void givenValidPatientId_whenGetTrainingSessionsFromPatient_thenReturnsEmptyList() {
        PagedResponse<TrainingSessionSummaryElement> pagedResponse = new PagedResponse<>(
                List.of(), 0, 0, 0, true
        );

        given(getTrainingSessionByPatientUseCase.getTrainingSessionFromPatient(any(PageQuery.class), eq(TEST_PATIENT.getId())))
                .willReturn(pagedResponse);

        RestAssuredMockMvc.given()
                .param("page", "0")
                .param("size", "10")
                .when()
                .get("/training-session/{patientId}", String.valueOf(TEST_PATIENT.getId().value()))
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void givenInvalidPatientId_whenGetTrainingSessionsFromPatient_thenReturnsNotFound() {
        given(getTrainingSessionByPatientUseCase.getTrainingSessionFromPatient(any(PageQuery.class), eq(TEST_PATIENT.getId())))
                .willThrow(new InvalidIdException());

        RestAssuredMockMvc.given()
                .param("page", "0")
                .param("size", "10")
                .when()
                .get("/training-session/{patientId}", String.valueOf(TEST_PATIENT.getId().value()))
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
