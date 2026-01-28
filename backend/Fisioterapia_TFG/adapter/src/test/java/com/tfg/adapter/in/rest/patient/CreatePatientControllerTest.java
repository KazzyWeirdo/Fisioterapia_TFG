package com.tfg.adapter.in.rest.patient;

import com.tfg.port.in.patient.CreatePatientUseCase;
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

@ExtendWith(MockitoExtension.class)
public class CreatePatientControllerTest {

    @Mock
    private CreatePatientUseCase createPatientUseCase;

    @InjectMocks
    private CreatePatientController createPatientController;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(createPatientController);
    }

    @Test
    void createPatient_ShouldReturnOk_WhenInputIsValid() throws Exception {
        PatientCreationModel patientCreationModel = new PatientCreationModel(
                "test@example.com",
                "12345678A",
                "MALE",
                "John",
                "Doe",
                "Smith",
                123456789,
                LocalDate.of(1990, 1, 1)

        );

        given()
                .contentType("application/json")
                .body(patientCreationModel)
                .when()
                .post("/patients/create")
                .then()
                .status(HttpStatus.OK);
    }

    @Test
    void createPatient_ShouldReturnBadRequest_WhenInputIsInvalid() {
        PatientCreationModel invalidPatientCreationModel = new PatientCreationModel(
                "", // Email inválido
                "12345678A", "MALE", "John", "Doe", "Smith", 123456789,
                LocalDate.of(1990, 1, 1)
        );

        given()
                .contentType("application/json")
                .body(invalidPatientCreationModel)
                .when()
                .post("/patients/create")
                .then()
                .status(HttpStatus.BAD_REQUEST);
    }
}
