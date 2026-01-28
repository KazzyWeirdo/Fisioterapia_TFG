package com.tfg.adapter.in.rest.indiba;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.exceptions.InvalidIdException;
import com.tfg.indiba.IndibaSession;
import com.tfg.indiba.IndibaSessionId;
import com.tfg.model.indiba.IndibaSessionFactory;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.port.in.indiba.GetIndibaSessionUseCase;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GetIndibaSessionControllerTest {

    @Mock
    private GetIndibaSessionUseCase getIndibaSessionUseCase;

    @InjectMocks
    private GetIndibaSessionController getIndibaSessionController;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final IndibaSession TEST_INDIBA_SESSION_1 = new IndibaSessionFactory().createTestIndibaSession(TEST_PATIENT, new Date(2023, 11, 30), new Date(2023, 12, 15));

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getIndibaSessionController)
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    public void givenValidId_whenGetIndibaSession_thenReturnsIndibaSession() {
        given(getIndibaSessionUseCase.getIndibaSession(TEST_INDIBA_SESSION_1.getId()))
                .willReturn(TEST_INDIBA_SESSION_1);

        RestAssuredMockMvc.given()
                .when()
                .get("/indiba/{sessionId}", String.valueOf(TEST_INDIBA_SESSION_1.getId().value()))
                .then()
                .statusCode(200);
    }

    @Test
    public void givenInvalidId_whenGetIndibaSession_thenReturnsNotFound() {
        IndibaSessionId indibaSessionId = new IndibaSessionId(9999);
        given(getIndibaSessionUseCase.getIndibaSession(indibaSessionId))
                .willThrow(new InvalidIdException());

        RestAssuredMockMvc.given()
                .when()
                .get("/indiba/{sessionId}", String.valueOf(indibaSessionId.value()))
                .then()
                .statusCode(404);
    }
}
