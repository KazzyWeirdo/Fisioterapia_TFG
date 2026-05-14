package com.tfg.adapter.in.rest.statistics;

import com.tfg.adapter.in.rest.common.PatientIdParser;
import com.tfg.patient.PatientId;
import com.tfg.port.in.statistics.GetWorkloadProgressionUseCase;
import com.tfg.statistics.WorkloadProgression;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics/")
public class GetWorkloadProgressionController {

    private final GetWorkloadProgressionUseCase getWorkloadProgressionUseCase;

    public GetWorkloadProgressionController(GetWorkloadProgressionUseCase getWorkloadProgressionUseCase) {
        this.getWorkloadProgressionUseCase = getWorkloadProgressionUseCase;
    }

    @GetMapping("/{patientId}/{exerciseName}/workload-progression")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workload progerssion of exercise found successfully"),
            @ApiResponse(responseCode = "204", description = "There is no workload progression of this exercise"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<List<WorkloadProgression>> getWorkloadProgression(@PathVariable("patientId")String grabbedPatientId,
                                                                      @PathVariable("exerciseName") String exerciseName) {
        PatientId patientId = PatientIdParser.parsePatientId(grabbedPatientId);
        List<WorkloadProgression> progression = getWorkloadProgressionUseCase.calculateProgression(patientId, exerciseName)
                .stream()
                .toList();

        if(progression.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(progression);
    }
}
