package com.tfg.adapter.in.rest.physiotherapist;

import com.tfg.port.in.physiotherapist.RegisterPhysiotherapistUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/psychiatrist")
public class RegisterPhysiotherapistController {
    private final RegisterPhysiotherapistUseCase registerPsychiatristUseCase;

    public RegisterPhysiotherapistController(RegisterPhysiotherapistUseCase registerPsychiatristUseCase) {
        this.registerPsychiatristUseCase = registerPsychiatristUseCase;
    }

    @PostMapping("/register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Psychiatrist registrated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Void> createPatient(@RequestBody @Valid RegisterPhysiotherapistModel registerPsychiatristModel) {
        registerPsychiatristUseCase.registerPsychiatrist(registerPsychiatristModel.toDomainModel());
        return ResponseEntity.ok().build();
    }
}
