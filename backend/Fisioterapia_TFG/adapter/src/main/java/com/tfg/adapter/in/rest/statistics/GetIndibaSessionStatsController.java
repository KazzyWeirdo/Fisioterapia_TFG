package com.tfg.adapter.in.rest.statistics;

import com.tfg.model.patient.PatientId;
import com.tfg.application.port.in.statistics.GetIndibaSessionStatsUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
public class GetIndibaSessionStatsController {

    private final GetIndibaSessionStatsUseCase getIndibaSessionStatsUseCase;

    public GetIndibaSessionStatsController(GetIndibaSessionStatsUseCase getIndibaSessionStatsUseCase) {
        this.getIndibaSessionStatsUseCase = getIndibaSessionStatsUseCase;
    }

    @GetMapping("/{id}/indiba-stats")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "INDIBA statistics for the patient")
    })
    public ResponseEntity<IndibaSessionStatsWebModel> getIndibaStats(@PathVariable int id) {
        return ResponseEntity.ok(
                IndibaSessionStatsWebModel.from(
                        getIndibaSessionStatsUseCase.getIndibaSessionStats(new PatientId(id))
                )
        );
    }
}
