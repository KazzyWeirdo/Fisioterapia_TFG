package com.tfg.adapter.in.rest.trainingsession;

import com.tfg.application.port.in.trainingsession.CreateTrainingSessionUseCase;
import com.tfg.application.port.out.persistence.ExerciseTemplateRepository;
import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.application.port.out.persistence.PhysiotherapistRepository;
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
    private final PhysiotherapistRepository physiotherapistRepository;
    private final ExerciseTemplateRepository exerciseTemplateRepository;

    public CreateTrainingSessionController(CreateTrainingSessionUseCase createTrainingSessionUseCase,
                                           PatientRepository patientRepository,
                                           PhysiotherapistRepository physiotherapistRepository,
                                           ExerciseTemplateRepository exerciseTemplateRepository) {
        this.createTrainingSessionUseCase = createTrainingSessionUseCase;
        this.patientRepository = patientRepository;
        this.physiotherapistRepository = physiotherapistRepository;
        this.exerciseTemplateRepository = exerciseTemplateRepository;
    }

    @PostMapping("/create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training session created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Patient or physiotherapist not found")
    })
    public ResponseEntity<Void> createTrainingSession(@RequestBody @Valid TrainingSessionCreationModel trainingSessionCreationModel) {
        createTrainingSessionUseCase.createTrainingSession(
                trainingSessionCreationModel.toDomainModel(patientRepository, physiotherapistRepository, exerciseTemplateRepository));
        return ResponseEntity.ok().build();
    }
}
