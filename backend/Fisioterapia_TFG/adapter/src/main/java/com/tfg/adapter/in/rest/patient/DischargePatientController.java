package com.tfg.adapter.in.rest.patient;

import com.tfg.model.patient.PatientId;
import com.tfg.application.port.in.patient.DischargePatientUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
public class DischargePatientController {

    private final DischargePatientUseCase dischargePatientUseCase;

    public DischargePatientController(DischargePatientUseCase dischargePatientUseCase) {
        this.dischargePatientUseCase = dischargePatientUseCase;
    }

    @PatchMapping("/{id}/discharge")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient discharged"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<Void> discharge(@PathVariable int id) {
        dischargePatientUseCase.discharge(new PatientId(id));
        return ResponseEntity.ok().build();
    }
}
