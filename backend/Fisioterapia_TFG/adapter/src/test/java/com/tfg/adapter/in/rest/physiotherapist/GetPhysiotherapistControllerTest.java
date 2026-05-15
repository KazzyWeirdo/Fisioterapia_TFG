package com.tfg.adapter.in.rest.physiotherapist;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.application.port.in.physiotherapist.GetPhysiotherapistUseCase;
import com.tfg.model.physiotherapist.Physiotherapist;
import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import com.tfg.model.physiotherapist.PhysiotherapistId;
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
public class GetPhysiotherapistControllerTest {

    @Mock
    private GetPhysiotherapistUseCase getPhysiotherapistUseCase;

    @InjectMocks
    private GetPhysiotherapistController getPhysiotherapistController;

    private static final Physiotherapist TEST_PHYSIO =
            PhysiotherapistFactory.createTestPsychiatrist("physio@test.com", "password123");

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getPhysiotherapistController)
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    void givenExistingId_getPhysiotherapist_returns200() {
        PhysiotherapistId id = TEST_PHYSIO.getId();
        given(getPhysiotherapistUseCase.getPhysiotherapist(id)).willReturn(TEST_PHYSIO);

        RestAssuredMockMvc.given()
                .when()
                .get("/physiotherapist/{id}", id.value())
                .then()
                .statusCode(200);
    }

    @Test
    void givenNonExistingId_getPhysiotherapist_returns404() {
        PhysiotherapistId invalidId = new PhysiotherapistId(999999);
        given(getPhysiotherapistUseCase.getPhysiotherapist(invalidId)).willThrow(new InvalidIdException());

        RestAssuredMockMvc.given()
                .when()
                .get("/physiotherapist/{id}", 999999)
                .then()
                .statusCode(404);
    }
}
