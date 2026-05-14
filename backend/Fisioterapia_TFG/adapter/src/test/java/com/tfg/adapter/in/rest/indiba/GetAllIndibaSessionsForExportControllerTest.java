package com.tfg.adapter.in.rest.indiba;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.indiba.IndibaSession;
import com.tfg.model.indiba.IndibaSessionFactory;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import com.tfg.patient.Patient;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.port.in.indiba.GetAllIndibaSessionsForExportUseCase;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GetAllIndibaSessionsForExportControllerTest {

    @Mock
    private GetAllIndibaSessionsForExportUseCase getAllIndibaSessionsForExportUseCase;

    @InjectMocks
    private GetAllIndibaSessionsForExportController getAllIndibaSessionsForExportController;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final Physiotherapist TEST_PHYSIO = PhysiotherapistFactory.createTestPsychiatrist("physio@gmail.com", "ValidPassword123!");
    private static final IndibaSession TEST_SESSION = IndibaSessionFactory.createTestIndibaSession(
            TEST_PATIENT, TEST_PHYSIO, new Date(2024, 1, 10), new Date(2024, 1, 10));

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getAllIndibaSessionsForExportController)
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    public void givenSessionsExist_whenExport_thenReturns200() {
        given(getAllIndibaSessionsForExportUseCase.getAllIndibaSessionsForExport()).willReturn(List.of(TEST_SESSION));

        RestAssuredMockMvc.when()
                .get("/indiba/export")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void givenNoSessions_whenExport_thenReturns204() {
        given(getAllIndibaSessionsForExportUseCase.getAllIndibaSessionsForExport()).willReturn(List.of());

        RestAssuredMockMvc.when()
                .get("/indiba/export")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
