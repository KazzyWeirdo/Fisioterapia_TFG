package com.tfg.adapter.in.rest.indiba;

import com.tfg.port.in.indiba.CreateIndibaSessionUseCase;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.PhysiotherapistRepository;
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
    private final PhysiotherapistRepository physiotherapistRepository;

    public CreateIndibaSessionController(CreateIndibaSessionUseCase indibaSessionUseCase, PatientRepository patientRepository, PhysiotherapistRepository physiotherapistRepository) {
        this.indibaSessionUseCase = indibaSessionUseCase;
        this.patientRepository = patientRepository;
        this.physiotherapistRepository = physiotherapistRepository;
    }

    @PostMapping("/create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indiba session created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Void> createIndibaSession(@RequestBody @Valid IndibaCreationModel indibaCreationModel) {
        indibaSessionUseCase.createIndibaSession(indibaCreationModel.toDomainModel(patientRepository, physiotherapistRepository));
        return ResponseEntity.ok().build();
    }
}
