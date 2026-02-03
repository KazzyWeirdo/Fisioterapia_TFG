package com.tfg.adapter.in.rest.pni;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.pni.PniReportFactory;
import com.tfg.patient.Patient;
import com.tfg.pni.PniReport;
import com.tfg.pni.PniReportId;
import com.tfg.port.in.pni.GetPniReportUseCase;
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
public class GetPniReportControllerTest {

    @Mock
    private GetPniReportUseCase getPniReportUseCase;

    @InjectMocks
    private GetPniReportController getPniReportController;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final PniReport TEST_PNI_REPORT = PniReportFactory.createTestPniReport(TEST_PATIENT, 5);

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getPniReportController)
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    public void givenValidId_whenGetPniReport_thenReturnsPniReport() {
        given(getPniReportUseCase.getPniReport(TEST_PNI_REPORT.getId()))
                .willReturn(TEST_PNI_REPORT);

        RestAssuredMockMvc.given()
                .when()
                .get("/pni/{reportId}", String.valueOf(TEST_PNI_REPORT.getId().value()))
                .then()
                .statusCode(200);
    }

    @Test
    public void givenInvalidId_whenGetPniReport_thenReturnsNotFound() {
        PniReportId invalidPniReportId = new PniReportId(9999);
        given(getPniReportUseCase.getPniReport(invalidPniReportId))
                .willThrow(new com.tfg.exceptions.InvalidIdException());

        RestAssuredMockMvc.given()
                .when()
                .get("/pni/{reportId}", String.valueOf(invalidPniReportId.value()))
                .then()
                .statusCode(404);
    }
}
