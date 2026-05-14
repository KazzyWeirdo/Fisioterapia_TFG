package com.tfg.adapter.in.rest.pni;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.pni.PniReportFactory;
import com.tfg.patient.Patient;
import com.tfg.pni.PniReport;
import com.tfg.port.in.pni.GetAllPniReportsForExportUseCase;
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
public class GetAllPniReportsForExportControllerTest {

    @Mock
    private GetAllPniReportsForExportUseCase getAllPniReportsForExportUseCase;

    @InjectMocks
    private GetAllPniReportsForExportController getAllPniReportsForExportController;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final PniReport TEST_REPORT = PniReportFactory.createTestPniReport(TEST_PATIENT, 75);

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getAllPniReportsForExportController)
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    public void givenReportsExist_whenExport_thenReturns200() {
        given(getAllPniReportsForExportUseCase.getAllPniReportsForExport()).willReturn(List.of(TEST_REPORT));

        RestAssuredMockMvc.when()
                .get("/pni/export")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void givenNoReports_whenExport_thenReturns204() {
        given(getAllPniReportsForExportUseCase.getAllPniReportsForExport()).willReturn(List.of());

        RestAssuredMockMvc.when()
                .get("/pni/export")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
