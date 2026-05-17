package com.tfg.adapter.in.rest.polar;

import com.tfg.adapter.in.rest.common.PatientIdParser;
import com.tfg.application.port.in.polar.SyncPolarDataForPatientUseCase;
import com.tfg.model.patient.PatientId;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/polar")
@RequiredArgsConstructor
public class SyncPolarDataController {

    private final SyncPolarDataForPatientUseCase syncPolarDataUseCase;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sync completed successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found or no Polar connection"),
            @ApiResponse(responseCode = "400", description = "Polar API error")
    })
    @PostMapping("/sync/{patientId}")
    public ResponseEntity<Void> syncForPatient(@PathVariable String patientId) {
        PatientId id = PatientIdParser.parsePatientId(patientId);
        syncPolarDataUseCase.syncForPatient(id);
        return ResponseEntity.noContent().build();
    }
}
