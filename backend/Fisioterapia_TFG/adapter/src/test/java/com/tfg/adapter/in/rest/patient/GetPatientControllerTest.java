package com.tfg.adapter.in.rest.patient;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import com.tfg.port.in.patient.GetPatientUseCase;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class GetPatientControllerTest {

    @Mock
    private GetPatientUseCase getPatientUseCase;

    @InjectMocks
    private GetPatientController getPatientController;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getPatientController)
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    public void givenValidId_whenGetPatient_thenReturnsPatient() throws Exception {
        given(getPatientUseCase.getPatient(new PatientId(TEST_PATIENT.getId().value())))
                .willReturn(TEST_PATIENT);

        given()
                .when()
                .get("/patients/{patientId}", String.valueOf(TEST_PATIENT.getId().value()))
                .then()
                .status(OK)
                .body("id", equalTo(TEST_PATIENT.getId().value()))
                .body("name", equalTo(TEST_PATIENT.getName()))
                .body("email", equalTo(TEST_PATIENT.getEmail().value()));
    }

    @Test
    public void givenInvalidId_whenGetPatient_thenReturnsNotFound() throws Exception {
        PatientId invalidId = new PatientId(9999);
        given(getPatientUseCase.getPatient(invalidId))
                .willThrow(new InvalidIdException());

        given()
                .when()
                .get("/patients/{patientId}", String.valueOf(invalidId.value()))
                .then()
                .status(NOT_FOUND);
    }
}
