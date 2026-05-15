package com.tfg.adapter.in.rest.statistics;

import com.tfg.application.port.in.statistics.GetPathologyRehabilitationStatsUseCase;
import com.tfg.model.statistics.PathologyRehabilitationStats;
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
import static org.hamcrest.Matchers.*;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;

@ExtendWith(MockitoExtension.class)
public class GetPathologyRehabilitationStatsControllerTest {

    @Mock
    private GetPathologyRehabilitationStatsUseCase getPathologyRehabilitationStatsUseCase;

    @InjectMocks
    private GetPathologyRehabilitationStatsController getPathologyRehabilitationStatsController;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getPathologyRehabilitationStatsController)
        );
    }

    @Test
    public void getRehabStats_ShouldReturnOk_WithPathologyEntries() {
        List<PathologyRehabilitationStats> stats = List.of(
                new PathologyRehabilitationStats("KNEE_OSTEOARTHRITIS", 42.5, 3)
        );
        given(getPathologyRehabilitationStatsUseCase.getPathologyRehabilitationStats()).willReturn(stats);

        when()
                .get("/statistics/rehabilitation-by-pathology")
                .then()
                .status(HttpStatus.OK)
                .body("[0].pathology", equalTo("KNEE_OSTEOARTHRITIS"))
                .body("[0].sampleSize", equalTo(3));
    }

    @Test
    public void getRehabStats_ShouldReturnEmptyList_WhenNoDischargedPatients() {
        given(getPathologyRehabilitationStatsUseCase.getPathologyRehabilitationStats()).willReturn(List.of());

        when()
                .get("/statistics/rehabilitation-by-pathology")
                .then()
                .status(HttpStatus.OK)
                .body("$", hasSize(0));
    }
}
