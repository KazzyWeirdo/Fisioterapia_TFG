package com.tfg.adapter.in.rest.patient;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.exceptions.InvalidIdException;
import com.tfg.port.in.patient.UpdatePatientUseCase;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class UpdatePatientControllerTest {

    @Mock
    private UpdatePatientUseCase updatePatientUseCase;

    @InjectMocks
    private UpdatePatientController updatePatientController;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                org.springframework.test.web.servlet.setup.MockMvcBuilders
                        .standaloneSetup(updatePatientController)
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    void updatePatient_ShouldReturnOk_WhenInputIsValid() throws Exception {
        PatientCreationModel patientCreationModel = new PatientCreationModel(
                "updated@example.com",
                "12345678A",
                "FEMALE",
                "Jane",
                "Doe",
                "Smith",
                987654321,
                LocalDate.of(1985, 5, 15)
        );

        given()
                .contentType("application/json")
                .body(patientCreationModel)
                .when()
                .put("/patients/1")
                .then()
                .status(HttpStatus.OK);
    }

    @Test
    void updatePatient_ShouldReturnBadRequest_WhenInputIsInvalid() {
        PatientCreationModel invalidPatientCreationModel = new PatientCreationModel(
                "", // Invalid email
                "12345678A",
                "FEMALE",
                "Jane",
                "Doe",
                "Smith",
                987654321,
                LocalDate.of(1985, 5, 15)
        );

        given()
                .contentType("application/json")
                .body(invalidPatientCreationModel)
                .when()
                .put("/patients/1")
                .then()
                .status(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updatePatient_ShouldReturnNotFound_WhenPatientDoesNotExist() {
        PatientCreationModel patientCreationModel = new PatientCreationModel(
                "updated@example.com",
                "12345678A",
                "FEMALE",
                "Jane",
                "Doe",
                "Smith",
                987654321,
                LocalDate.of(1985, 5, 15)
        );

        doThrow(new InvalidIdException())
                .when(updatePatientUseCase)
                .updatePatient(any(), any());

        given()
                .contentType("application/json")
                .body(patientCreationModel)
                .when()
                .put("/patients/999") // Non-existing patient ID
                .then()
                .status(HttpStatus.NOT_FOUND);
    }
}
