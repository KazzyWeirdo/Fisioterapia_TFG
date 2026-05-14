package com.tfg.adapter.in.rest.patient;

import com.tfg.application.port.in.patient.DeletePatientUseCase;
import com.tfg.model.patient.PatientId;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
public class DeletePatientController {

    private final DeletePatientUseCase deletePatientUseCase;

    public DeletePatientController(DeletePatientUseCase deletePatientUseCase) {
        this.deletePatientUseCase = deletePatientUseCase;
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient deleted"),
            @ApiResponse(responseCode = "403", description = "Forbidden — ADMIN scope required"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<Void> delete(@PathVariable int id) {
        deletePatientUseCase.delete(new PatientId(id));
        return ResponseEntity.noContent().build();
    }
}
