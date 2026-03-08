package com.tfg.adapter.in.rest.trainingsession;

import com.tfg.port.in.trainingsession.CreateTrainingSessionUseCase;
import com.tfg.port.out.persistence.PatientRepository;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/training-session")
public class CreateTrainingSessionController {

    private final CreateTrainingSessionUseCase createTrainingSessionUseCase;
    private final PatientRepository patientRepository;

    public CreateTrainingSessionController(CreateTrainingSessionUseCase createTrainingSessionUseCase, PatientRepository patientRepository) {
        this.createTrainingSessionUseCase = createTrainingSessionUseCase;
        this.patientRepository = patientRepository;
    }

    @PostMapping("/create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training session created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Void> createTrainingSession(@RequestBody @Valid TrainingSessionCreationModel trainingSessionCreationModel) {
        createTrainingSessionUseCase.createTrainingSession(trainingSessionCreationModel.toDomainModel(patientRepository));
        return ResponseEntity.ok().build();
    }
}
