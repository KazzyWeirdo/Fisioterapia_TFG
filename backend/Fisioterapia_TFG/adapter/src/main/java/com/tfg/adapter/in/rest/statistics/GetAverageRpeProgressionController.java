package com.tfg.adapter.in.rest.statistics;

import com.tfg.adapter.in.rest.common.PatientIdParser;
import com.tfg.model.patient.PatientId;
import com.tfg.application.port.in.statistics.GetAverageRpeProgressionUseCase;
import com.tfg.model.statistics.AverageRpeProgression;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics/")
public class GetAverageRpeProgressionController {

    private final GetAverageRpeProgressionUseCase getAverageRpeProgressionUseCase;

    public GetAverageRpeProgressionController(GetAverageRpeProgressionUseCase getAverageRpeProgressionUseCase) {
        this.getAverageRpeProgressionUseCase = getAverageRpeProgressionUseCase;
    }

    @GetMapping("/{patientId}/{exerciseName}/workload-progression")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workload progerssion of exercise found successfully"),
            @ApiResponse(responseCode = "204", description = "There is no workload progression of this exercise"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<List<AverageRpeProgression>> getAverageRpeProgression(@PathVariable("patientId")String grabbedPatientId,
                                                                      @PathVariable("exerciseName") String exerciseName) {
        PatientId patientId = PatientIdParser.parsePatientId(grabbedPatientId);
        List<AverageRpeProgression> progression = getAverageRpeProgressionUseCase.calculateProgression(patientId, exerciseName)
                .stream()
                .toList();

        if(progression.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(progression);
    }
}
