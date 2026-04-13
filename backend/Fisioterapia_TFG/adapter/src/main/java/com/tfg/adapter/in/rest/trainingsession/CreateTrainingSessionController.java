package com.tfg.adapter.in.rest.trainingsession;

import com.tfg.adapter.in.rest.common.PatientIdParser;
import com.tfg.patient.PatientId;
import com.tfg.port.in.trainingsession.CreateTrainingSessionUseCase;
import com.tfg.port.out.persistence.PatientRepository;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/training-session")
public class CreateTrainingSessionController {

    private final CreateTrainingSessionUseCase createTrainingSessionUseCase;
    private final PatientRepository patientRepository;

    public CreateTrainingSessionController(CreateTrainingSessionUseCase createTrainingSessionUseCase, PatientRepository patientRepository) {
        this.createTrainingSessionUseCase = createTrainingSessionUseCase;
        this.patientRepository = patientRepository;
    }

    @PostMapping("/{patientId}/create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training session created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<Void> createTrainingSession(@RequestBody @Valid TrainingSessionCreationModel trainingSessionCreationModel, @PathVariable("patientId") String grabbedPatientId) {
        PatientId patientId = PatientIdParser.parsePatientId(grabbedPatientId);
        createTrainingSessionUseCase.createTrainingSession(trainingSessionCreationModel.toDomainModel(patientRepository, patientId));
        return ResponseEntity.ok().build();
    }
}
