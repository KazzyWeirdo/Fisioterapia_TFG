package com.tfg.adapter.in.rest.patient;

import com.tfg.model.patient.PatientId;
import com.tfg.application.port.in.patient.UpdatePatientFunctionalScoreUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
public class UpdatePatientFunctionalScoreController {

    private final UpdatePatientFunctionalScoreUseCase updatePatientFunctionalScoreUseCase;

    public UpdatePatientFunctionalScoreController(UpdatePatientFunctionalScoreUseCase updatePatientFunctionalScoreUseCase) {
        this.updatePatientFunctionalScoreUseCase = updatePatientFunctionalScoreUseCase;
    }

    public record FunctionalScoreRequest(
            @NotNull(message = "Score is required")
            @Min(value = 0, message = "Score must be >= 0")
            @Max(value = 100, message = "Score must be <= 100")
            Integer score
    ) {}

    @PatchMapping("/{id}/functional-score")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Functional score updated"),
            @ApiResponse(responseCode = "400", description = "Score out of range"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<Void> updateFunctionalScore(
            @PathVariable int id,
            @RequestBody @Valid FunctionalScoreRequest request
    ) {
        updatePatientFunctionalScoreUseCase.updateFunctionalScore(new PatientId(id), request.score());
        return ResponseEntity.ok().build();
    }
}
