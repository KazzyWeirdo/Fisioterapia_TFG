package com.tfg.adapter.in.rest.exercisetemplate;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.application.port.in.exercisetemplate.GetExerciseTemplateByIdUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exercise-template")
public class GetExerciseTemplateByIdController {

    private final GetExerciseTemplateByIdUseCase getExerciseTemplateByIdUseCase;

    public GetExerciseTemplateByIdController(GetExerciseTemplateByIdUseCase getExerciseTemplateByIdUseCase) {
        this.getExerciseTemplateByIdUseCase = getExerciseTemplateByIdUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseTemplateWebModel> getById(@PathVariable int id) {
        return getExerciseTemplateByIdUseCase.getById(id)
                .map(ExerciseTemplateWebModel::from)
                .map(ResponseEntity::ok)
                .orElseThrow(InvalidIdException::new);
    }
}
