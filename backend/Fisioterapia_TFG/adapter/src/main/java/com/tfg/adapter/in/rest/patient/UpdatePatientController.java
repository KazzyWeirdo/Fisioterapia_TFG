package com.tfg.adapter.in.rest.patient;

import com.tfg.adapter.in.rest.common.PatientIdParser;
import com.tfg.patient.PatientId;
import com.tfg.port.in.patient.UpdatePatientUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
public class UpdatePatientController {

    private final UpdatePatientUseCase updatePatientUseCase;

    public UpdatePatientController(UpdatePatientUseCase updatePatientUseCase) {
        this.updatePatientUseCase = updatePatientUseCase;
    }

    @PutMapping("/{patientId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<Void> updatePatient(@PathVariable("patientId") String grabbedPatientId, @RequestBody @Valid PatientCreationModel patientCreationModel){
        PatientId patientId = PatientIdParser.parsePatientId(grabbedPatientId);
        updatePatientUseCase.updatePatient(patientId, patientCreationModel.toDomainModel());
        return ResponseEntity.ok().build();
    }
}
