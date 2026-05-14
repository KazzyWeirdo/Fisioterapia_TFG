package com.tfg.adapter.in.rest.indiba;

import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import com.tfg.model.patient.Patient;
import com.tfg.model.physiotherapist.Physiotherapist;
import com.tfg.application.port.in.indiba.CreateIndibaSessionUseCase;
import com.tfg.application.port.out.persistence.PhysiotherapistRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateIndibaSessionControllerTest {

    @Mock
    private CreateIndibaSessionUseCase createIndibaSessionUseCase;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PhysiotherapistRepository physiotherapistRepository;

    @InjectMocks
    private CreateIndibaSessionController createIndibaSessionController;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final Physiotherapist TEST_PHYSIOTHERAPIST = PhysiotherapistFactory.createTestPsychiatrist("hola@gmail.com", "ValidPassword1!");

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(createIndibaSessionController);
    }

    @Test
    void createIndibaSession_ShouldReturnOk_WhenInputIsValid() {
        when(patientRepository.findById(any()))
                .thenReturn(Optional.of(TEST_PATIENT));

        when(physiotherapistRepository.findById(any()))
                .thenReturn(Optional.of(TEST_PHYSIOTHERAPIST));

        IndibaCreationModel indibaCreationModel = new IndibaCreationModel(
                TEST_PATIENT.getId().value(),
                new java.util.Date(System.currentTimeMillis() - 100000),
                new java.util.Date(System.currentTimeMillis()),
                "Lower Back",
                "CAPACITIVE",
                5.0f,
                null,
                TEST_PHYSIOTHERAPIST.getId().value(),
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
    void createIndibaSession_ShouldReturnOk_WhenDualModeWithBothIntensities() {
        when(patientRepository.findById(any()))
                .thenReturn(Optional.of(TEST_PATIENT));

        when(physiotherapistRepository.findById(any()))
                .thenReturn(Optional.of(TEST_PHYSIOTHERAPIST));

        IndibaCreationModel indibaCreationModel = new IndibaCreationModel(
                TEST_PATIENT.getId().value(),
                new java.util.Date(System.currentTimeMillis() - 100000),
                new java.util.Date(System.currentTimeMillis()),
                "Lower Back",
                "DUAL",
                5.0f,
                4.0f,
                TEST_PHYSIOTHERAPIST.getId().value(),
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
                new java.util.Date(System.currentTimeMillis() + 10000),
                new java.util.Date(System.currentTimeMillis()),
                "Lower Back",
                "CAPACITIVE",
                5.0f,
                null,
                TEST_PHYSIOTHERAPIST.getId().value(),
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
