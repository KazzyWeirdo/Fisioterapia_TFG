package com.tfg.adapter.in.rest.patient;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.port.in.patient.GetAllPatientsForExportUseCase;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GetAllPatientsForExportControllerTest {

    @Mock
    private GetAllPatientsForExportUseCase getAllPatientsForExportUseCase;

    @InjectMocks
    private GetAllPatientsForExportController getAllPatientsForExportController;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getAllPatientsForExportController)
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    public void givenPatientsExist_whenExport_thenReturns200() {
        given(getAllPatientsForExportUseCase.getAllPatientsForExport()).willReturn(List.of(TEST_PATIENT));

        RestAssuredMockMvc.when()
                .get("/patients/export")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void givenNoPatients_whenExport_thenReturns204() {
        given(getAllPatientsForExportUseCase.getAllPatientsForExport()).willReturn(List.of());

        RestAssuredMockMvc.when()
                .get("/patients/export")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
