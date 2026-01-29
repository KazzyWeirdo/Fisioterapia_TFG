package com.tfg.adapter.in.rest.indiba;

import com.tfg.port.in.indiba.CreateIndibaSessionUseCase;
import com.tfg.port.out.persistence.PatientRepository;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/indiba")
public class CreateIndibaSessionController {

    private final CreateIndibaSessionUseCase indibaSessionUseCase;
    private final PatientRepository patientRepository;

    public CreateIndibaSessionController(CreateIndibaSessionUseCase indibaSessionUseCase, PatientRepository patientRepository) {
        this.indibaSessionUseCase = indibaSessionUseCase;
        this.patientRepository = patientRepository;
    }

    @PostMapping("/create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indiba session created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Void> createIndibaSession(@RequestBody @Valid IndibaCreationModel indibaCreationModel) {
        indibaSessionUseCase.createIndibaSession(indibaCreationModel.toDomainModel(patientRepository));
        return ResponseEntity.ok().build();
    }
}
