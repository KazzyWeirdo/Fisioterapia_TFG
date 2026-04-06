package com.tfg.adapter.in.rest.pni;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.PniReportSummaryElement;
import com.tfg.port.in.pni.GetPniReportsFromPatientUseCase;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class GetPniReportsFromPatientControllerTest {

    @Mock
    private GetPniReportsFromPatientUseCase getPniReportsFromPatientUseCase;

    @InjectMocks
    private GetPniReportsFromPatientController getPniReportsFromPatientController;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private final PniReportSummaryElement TEST_PNI_REPORT = new PniReportSummaryElement(1, LocalDate.of(2023, 12, 15));

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getPniReportsFromPatientController)
                        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    public void givenValidPatientId_whenGetPniReportsFromPatient_thenReturnsReports() {
        PagedResponse<PniReportSummaryElement> pagedResponse = new PagedResponse<>(
                List.of(TEST_PNI_REPORT), 1, 1, 0, true
        );

        given(getPniReportsFromPatientUseCase.getPniReportsFromPatient(any(PageQuery.class),eq(TEST_PATIENT.getId())))
                .willReturn(pagedResponse);

        RestAssuredMockMvc.given()
                .param("page", "0")
                .param("size", "10")
                .when()
                .get("/pni/{patientId}", String.valueOf(TEST_PATIENT.getId().value()))
                .then()
                .statusCode(200);
    }

    @Test
    public void givenInvalidPatientId_whenGetPniReportsFromPatient_thenReturnsBadRequest() {
        given(getPniReportsFromPatientUseCase.getPniReportsFromPatient(any(PageQuery.class), eq(null)))
                .willThrow(new InvalidIdException());

        RestAssuredMockMvc.given()
                .param("page", "0")
                .param("size", "10")
                .when()
                .get("/pni/{patientId}", "invalid-id")
                .then()
                .statusCode(404);
    }

    @Test
    public void givenPatientWithNoPniReports_whenGetPniReportsFromPatient_thenReturnsEmptyList() {
        PagedResponse<PniReportSummaryElement> pagedResponse = new PagedResponse<>(
                List.of(), 0, 0, 0, true
        );

        given(getPniReportsFromPatientUseCase.getPniReportsFromPatient(any(PageQuery.class), eq(TEST_PATIENT.getId())))
                .willReturn(pagedResponse);

        RestAssuredMockMvc.given()
                .param("page", "0")
                .param("size", "10")
                .when()
                .get("/pni/{patientId}", String.valueOf(TEST_PATIENT.getId().value()))
                .then()
                .statusCode(204);
    }
}
