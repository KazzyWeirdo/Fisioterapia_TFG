package com.tfg.adapter.in.rest.patient;

import com.tfg.patient.PatientGender;
import com.tfg.port.in.patient.CreatePatientUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
public class CreatePatientController {
    private final CreatePatientUseCase patientUseCase;

    public CreatePatientController(CreatePatientUseCase patientUseCase) {
        this.patientUseCase = patientUseCase;
    }

    @PostMapping("/create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
        })
    public ResponseEntity<Void> createPatient(@RequestBody @Valid PatientCreationModel patientCreationModel) {
        patientUseCase.createPatient(patientCreationModel.toDomainModel());
        return ResponseEntity.ok().build();
    }
}
