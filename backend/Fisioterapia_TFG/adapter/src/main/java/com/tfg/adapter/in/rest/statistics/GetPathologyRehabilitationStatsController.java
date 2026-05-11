package com.tfg.adapter.in.rest.statistics;

import com.tfg.application.port.in.statistics.GetPathologyRehabilitationStatsUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class GetPathologyRehabilitationStatsController {

    private final GetPathologyRehabilitationStatsUseCase getPathologyRehabilitationStatsUseCase;

    public GetPathologyRehabilitationStatsController(GetPathologyRehabilitationStatsUseCase getPathologyRehabilitationStatsUseCase) {
        this.getPathologyRehabilitationStatsUseCase = getPathologyRehabilitationStatsUseCase;
    }

    @GetMapping("/rehabilitation-by-pathology")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Average rehabilitation days by pathology")
    })
    public ResponseEntity<List<PathologyRehabilitationStatsWebModel>> getRehabStats() {
        return ResponseEntity.ok(
                getPathologyRehabilitationStatsUseCase.getPathologyRehabilitationStats()
                        .stream()
                        .map(PathologyRehabilitationStatsWebModel::from)
                        .toList()
        );
    }
}
