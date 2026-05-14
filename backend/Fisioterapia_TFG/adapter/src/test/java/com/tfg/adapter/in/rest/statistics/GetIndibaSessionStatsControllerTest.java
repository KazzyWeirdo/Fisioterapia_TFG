package com.tfg.adapter.in.rest.statistics;

import com.tfg.model.patient.PatientId;
import com.tfg.application.port.in.statistics.GetIndibaSessionStatsUseCase;
import com.tfg.model.statistics.IndibaSessionStats;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.equalTo;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;

@ExtendWith(MockitoExtension.class)
public class GetIndibaSessionStatsControllerTest {

    @Mock
    private GetIndibaSessionStatsUseCase getIndibaSessionStatsUseCase;

    @InjectMocks
    private GetIndibaSessionStatsController getIndibaSessionStatsController;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getIndibaSessionStatsController)
        );
    }

    @Test
    public void getIndibaStats_ShouldReturnOk_WhenPatientExists() {
        IndibaSessionStats stats = new IndibaSessionStats(3, 45.0, "Lumbar", 120.0, null, Map.of("CAPACITIVE", 3L));
        given(getIndibaSessionStatsUseCase.getIndibaSessionStats(any(PatientId.class))).willReturn(stats);

        when()
                .get("/patients/1/indiba-stats")
                .then()
                .status(HttpStatus.OK)
                .body("totalSessions", equalTo(3))
                .body("avgDurationMinutes", equalTo(45.0f))
                .body("mostTreatedArea", equalTo("Lumbar"));
    }

    @Test
    public void getIndibaStats_ShouldReturnZeroStats_WhenNoSessionsExist() {
        IndibaSessionStats empty = new IndibaSessionStats(0, 0.0, null, null, null, Map.of());
        given(getIndibaSessionStatsUseCase.getIndibaSessionStats(any(PatientId.class))).willReturn(empty);

        when()
                .get("/patients/1/indiba-stats")
                .then()
                .status(HttpStatus.OK)
                .body("totalSessions", equalTo(0));
    }
}
