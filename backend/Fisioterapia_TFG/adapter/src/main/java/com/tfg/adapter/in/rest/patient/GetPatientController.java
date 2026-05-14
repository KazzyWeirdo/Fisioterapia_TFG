package com.tfg.adapter.in.rest.patient;

import com.tfg.adapter.in.rest.common.PatientIdParser;
import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import com.tfg.port.in.patient.GetPatientUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
public class GetPatientController {
    private final GetPatientUseCase patientUseCase;

    public GetPatientController(GetPatientUseCase patientUseCase) {
        this.patientUseCase = patientUseCase;
    }

    @GetMapping("/{patientId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient found successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
        })
    public ResponseEntity<PatientWebModel> getPatient(@PathVariable("patientId") String grabbedPatientId) {
        PatientId patientId = PatientIdParser.parsePatientId(grabbedPatientId);
        Patient patient = patientUseCase.getPatient(patientId);
        return ResponseEntity.ok(PatientWebModel.fromDomainModel(patient));
    }
}
