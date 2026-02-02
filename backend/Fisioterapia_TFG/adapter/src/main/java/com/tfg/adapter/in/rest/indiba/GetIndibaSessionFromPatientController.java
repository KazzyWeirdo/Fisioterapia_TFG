package com.tfg.adapter.in.rest.indiba;

import com.tfg.adapter.in.rest.common.PatientIdParser;
import com.tfg.patient.PatientId;
import com.tfg.port.in.indiba.GetIndibaSessionFromPatientUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/indiba")
public class GetIndibaSessionFromPatientController {

    private final GetIndibaSessionFromPatientUseCase getIndibaSessionFromPatientUseCase;

    public GetIndibaSessionFromPatientController(GetIndibaSessionFromPatientUseCase getIndibaSessionFromPatientUseCase) {
        this.getIndibaSessionFromPatientUseCase = getIndibaSessionFromPatientUseCase;
    }

    @GetMapping("/{patientId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient found successfully"),
            @ApiResponse(responseCode = "204", description = "No Indiba sessions found for the patient"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<List<Date>> getIndibaSessionsFromPatient(@PathVariable("patientId") String grabbedPatientId) {
        PatientId patientId = PatientIdParser.parsePatientId(grabbedPatientId);
        List<Date> indibaSessions = getIndibaSessionFromPatientUseCase.getIndibaSessionsFromPatient(patientId)
                .stream()
                .toList();
        if (indibaSessions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(indibaSessions);
    }
}
