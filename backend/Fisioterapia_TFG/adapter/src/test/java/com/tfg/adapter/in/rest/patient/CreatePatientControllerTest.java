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
                "updated@example.com",
                "12345678A",
                "FEMALE",
                "FEMALE",
                "FEMALE",
                "Jane",
                "Jane",
                "Doe",
                "Smith",
                "She/Her",
                987654321,
                LocalDate.of(1985, 5, 15)

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
                "", // Invalid email
                "12345678A",
                "FEMALE",
                "FEMALE",
                "FEMALE",
                "Jane",
                "Jane",
                "Doe",
                "Smith",
                "She/Her",
                987654321,
                LocalDate.of(1985, 5, 15)
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
