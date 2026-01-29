package com.tfg.adapter.in.rest.indiba;

import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import com.tfg.port.in.indiba.CreateIndibaSessionUseCase;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateIndibaSessionControllerTest {

    @Mock
    private CreateIndibaSessionUseCase createIndibaSessionUseCase;

    @Mock
    private com.tfg.port.out.persistence.PatientRepository patientRepository;

    @InjectMocks
    private CreateIndibaSessionController createIndibaSessionController;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");

    @BeforeEach
    void setUp() {
        // Configurar RestAssuredMockMvc con el controlador a probar
        RestAssuredMockMvc.standaloneSetup(createIndibaSessionController);
    }

    @Test
    void createIndibaSession_ShouldReturnOk_WhenInputIsValid() {
        when(patientRepository.findById(any()))
                .thenReturn(Optional.of(TEST_PATIENT));

        IndibaCreationModel indibaCreationModel = new IndibaCreationModel(
                TEST_PATIENT.getId().value(),
                new java.util.Date(System.currentTimeMillis() - 100000), // beginSession en el pasado
                new java.util.Date(System.currentTimeMillis()), // endSession en el presente
                "Lower Back",
                "CAPACITIVE",
                5.0f,
                "Pain Relief",
                "Dr. Smith",
                "No observations"
        );
        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(indibaCreationModel)
                .when()
                .post("/indiba/create")
                .then()
                .statusCode(200);
    }

    @Test
    void createIndibaSession_ShouldReturnBadRequest_WhenInputIsInvalid() {
        IndibaCreationModel indibaCreationModel = new IndibaCreationModel(
                TEST_PATIENT.getId().value(),
                new java.util.Date(System.currentTimeMillis() + 10000), // beginSession en el pasado
                new java.util.Date(System.currentTimeMillis()), // endSession en el presente
                "Lower Back",
                "CAPACITIVE",
                5.0f,
                "Pain Relief",
                "Dr. Smith",
                "No observations"
        );

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(indibaCreationModel)
                .when()
                .post("/indiba/create")
                .then()
                .statusCode(400);
    }
}
