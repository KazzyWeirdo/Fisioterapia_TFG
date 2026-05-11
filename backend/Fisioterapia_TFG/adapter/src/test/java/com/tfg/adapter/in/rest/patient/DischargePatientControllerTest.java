package com.tfg.adapter.in.rest.patient;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientId;
import com.tfg.application.port.in.patient.DischargePatientUseCase;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

@ExtendWith(MockitoExtension.class)
public class DischargePatientControllerTest {

    @Mock
    private DischargePatientUseCase dischargePatientUseCase;

    @InjectMocks
    private DischargePatientController dischargePatientController;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(dischargePatientController)
                        .setControllerAdvice(new com.tfg.adapter.in.rest.common.GlobalExceptionHandler())
        );
    }

    @Test
    public void discharge_ShouldReturnOk_WhenPatientExists() {
        given()
                .when()
                .patch("/patients/1/discharge")
                .then()
                .status(HttpStatus.OK);

        verify(dischargePatientUseCase).discharge(new PatientId(1));
    }

    @Test
    public void discharge_ShouldReturnOk_WhenPatientAlreadyDischarged() {
        given()
                .when()
                .patch("/patients/1/discharge")
                .then()
                .status(HttpStatus.OK);
    }

    @Test
    public void discharge_ShouldReturnNotFound_WhenPatientDoesNotExist() {
        doThrow(new InvalidIdException())
                .when(dischargePatientUseCase).discharge(any(PatientId.class));

        given()
                .when()
                .patch("/patients/999/discharge")
                .then()
                .status(HttpStatus.NOT_FOUND);
    }
}
