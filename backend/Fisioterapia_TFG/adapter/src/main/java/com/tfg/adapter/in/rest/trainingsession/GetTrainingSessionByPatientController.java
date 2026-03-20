package com.tfg.adapter.in.rest.trainingsession;

import com.tfg.adapter.in.rest.common.PatientIdParser;
import com.tfg.patient.PatientId;
import com.tfg.port.in.trainingsession.GetTrainingSessionByPatientUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/training-session")
public class GetTrainingSessionByPatientController {

    private final GetTrainingSessionByPatientUseCase getTrainingSessionByPatientUseCase;

    public GetTrainingSessionByPatientController(GetTrainingSessionByPatientUseCase getTrainingSessionByPatientUseCase) {
        this.getTrainingSessionByPatientUseCase = getTrainingSessionByPatientUseCase;
    }

    @GetMapping("/{patientId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training sessions found successfully"),
            @ApiResponse(responseCode = "204", description = "No training sessions found for the patient"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<List<TrainingSessionListWebModel>> getTrainingSessionsByPatient(@PathVariable("patientId") String grabbedPatientId) {
        PatientId patientId = PatientIdParser.parsePatientId(grabbedPatientId);
        List<TrainingSessionListWebModel> trainingSessions = getTrainingSessionByPatientUseCase.getTrainingSessionFromPatient(patientId)
                .stream()
                .map(TrainingSessionListWebModel::fromDomainModel)
                .toList();
        if (trainingSessions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(trainingSessions);
    }
}
