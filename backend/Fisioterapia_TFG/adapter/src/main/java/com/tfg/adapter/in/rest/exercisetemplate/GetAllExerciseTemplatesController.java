package com.tfg.adapter.in.rest.exercisetemplate;

import com.tfg.port.in.exercisetemplate.GetAllExerciseTemplatesUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exercise-template")
public class GetAllExerciseTemplatesController {

    private final GetAllExerciseTemplatesUseCase getAllExerciseTemplatesUseCase;

    public GetAllExerciseTemplatesController(GetAllExerciseTemplatesUseCase getAllExerciseTemplatesUseCase) {
        this.getAllExerciseTemplatesUseCase = getAllExerciseTemplatesUseCase;
    }

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of exercise templates")
    })
    public ResponseEntity<List<ExerciseTemplateWebModel>> getAllExerciseTemplates() {
        return ResponseEntity.ok(
                getAllExerciseTemplatesUseCase.getAllExerciseTemplates().stream()
                        .map(ExerciseTemplateWebModel::from)
                        .toList()
        );
    }
}
