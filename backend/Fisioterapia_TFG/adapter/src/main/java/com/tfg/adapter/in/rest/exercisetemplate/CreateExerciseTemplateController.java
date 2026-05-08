package com.tfg.adapter.in.rest.exercisetemplate;

import com.tfg.port.in.exercisetemplate.CreateExerciseTemplateUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exercise-template")
public class CreateExerciseTemplateController {

    private final CreateExerciseTemplateUseCase createExerciseTemplateUseCase;

    public CreateExerciseTemplateController(CreateExerciseTemplateUseCase createExerciseTemplateUseCase) {
        this.createExerciseTemplateUseCase = createExerciseTemplateUseCase;
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercise template created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Void> createExerciseTemplate(@RequestBody @Valid ExerciseTemplateCreationModel exerciseTemplateCreationModel) {
        createExerciseTemplateUseCase.createExerciseTemplate(exerciseTemplateCreationModel.toDomainModel());
        return ResponseEntity.ok().build();
    }
}
