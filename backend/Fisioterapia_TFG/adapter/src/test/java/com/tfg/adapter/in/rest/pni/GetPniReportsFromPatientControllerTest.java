package com.tfg.adapter.in.rest.pni;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.pni.PniReportFactory;
import com.tfg.patient.Patient;
import com.tfg.pni.PniReport;
import com.tfg.port.in.pni.GetPniReportsFromPatientUseCase;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GetPniReportsFromPatientControllerTest {

    @Mock
    private GetPniReportsFromPatientUseCase getPniReportsFromPatientUseCase;

    @InjectMocks
    private GetPniReportsFromPatientController getPniReportsFromPatientController;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final PniReport TEST_PNI_REPORT = PniReportFactory.createTestPniReport(TEST_PATIENT, 5);
    private static final PniReport TEST_PNI_REPORT_2 = PniReportFactory.createTestPniReport(TEST_PATIENT, 8);

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getPniReportsFromPatientController)
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    public void givenValidPatientId_whenGetPniReportsFromPatient_thenReturnsReports() {
        given(getPniReportsFromPatientUseCase.getPniReportsFromPatient(TEST_PATIENT.getId()))
                .willReturn(java.util.List.of(TEST_PNI_REPORT.getReportDate(), TEST_PNI_REPORT_2.getReportDate()));

        RestAssuredMockMvc.given()
                .when()
                .get("/pni/{patientId}", String.valueOf(TEST_PATIENT.getId().value()))
                .then()
                .statusCode(200);
    }

    @Test
    public void givenInvalidPatientId_whenGetPniReportsFromPatient_thenReturnsBadRequest() {
        given(getPniReportsFromPatientUseCase.getPniReportsFromPatient(null))
                .willThrow(new InvalidIdException());

        RestAssuredMockMvc.given()
                .when()
                .get("/pni/{patientId}", "invalid-id")
                .then()
                .statusCode(404);
    }

    @Test
    public void givenPatientWithNoPniReports_whenGetPniReportsFromPatient_thenReturnsEmptyList() {
        given(getPniReportsFromPatientUseCase.getPniReportsFromPatient(TEST_PATIENT.getId()))
                .willReturn(java.util.Collections.emptyList());

        RestAssuredMockMvc.given()
                .when()
                .get("/pni/{patientId}", String.valueOf(TEST_PATIENT.getId().value()))
                .then()
                .statusCode(204);
    }
}
