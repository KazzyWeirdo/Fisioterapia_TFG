package com.tfg.adapter.in.rest.indiba;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.IndibaSummaryElement;
import com.tfg.port.in.indiba.GetIndibaSessionFromPatientUseCase;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GetIndibaSessionFromPatientControllerTest {

    @Mock
    private GetIndibaSessionFromPatientUseCase getIndibaSessionFromPatientUseCase;

    @InjectMocks
    private GetIndibaSessionFromPatientController getIndibaSessionFromPatientController;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private final IndibaSummaryElement TEST_INDIBA_SESSION_1 = new IndibaSummaryElement(1, new Date(2023, Calendar.DECEMBER, 15));

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getIndibaSessionFromPatientController)
                        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    public void givenValidPatientId_whenGetIndibaSessionsFromPatient_thenReturnsSessions() {
        PagedResponse<IndibaSummaryElement> pagedResponse = new PagedResponse<>(
                List.of(TEST_INDIBA_SESSION_1), 1, 1, 0, true
        );

        given(getIndibaSessionFromPatientUseCase.getIndibaSessionsFromPatient(any(PageQuery.class), eq(TEST_PATIENT.getId())))
                .willReturn(pagedResponse);

        RestAssuredMockMvc.given()
                .param("page", "0")
                .param("size", "10")
                .when()
                .get("/indiba/{patientId}", String.valueOf(TEST_PATIENT.getId().value()))
                .then()
                .statusCode(HttpStatus.OK.value());
    }


    @Test
    public void givenValidPatientId_whenGetIndibaSessionsFromPatient_thenReturnsNoContent() {
        PagedResponse<IndibaSummaryElement> pagedResponse = new PagedResponse<>(
                List.of(), 0, 0, 0, true
        );

        given(getIndibaSessionFromPatientUseCase.getIndibaSessionsFromPatient(any(PageQuery.class), eq(TEST_PATIENT.getId())))
                .willReturn(pagedResponse);

        RestAssuredMockMvc.given()
                .param("page", "0")
                .param("size", "10")
                .when()
                .get("/indiba/{patientId}", String.valueOf(TEST_PATIENT.getId().value()))
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void givenInvalidPatientId_whenGetIndibaSessionsFromPatient_thenReturnsNotFound() {
        given(getIndibaSessionFromPatientUseCase.getIndibaSessionsFromPatient(any(PageQuery.class), eq(TEST_PATIENT.getId())))
                .willThrow(new InvalidIdException());

        RestAssuredMockMvc.given()
                .param("page", "0")
                .param("size", "10")
                .when()
                .get("/indiba/{patientId}", String.valueOf(TEST_PATIENT.getId().value()))
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
