package com.tfg.adapter.in.rest.trainingsession;

import com.tfg.adapter.in.rest.common.TrainingSessionIdParser;
import com.tfg.port.in.trainingsession.GetTrainingSessionUseCase;
import com.tfg.trainingsession.TrainingSession;
import com.tfg.trainingsession.TrainingSessionId;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/training-session")
public class GetTrainingSessionController {

    private final GetTrainingSessionUseCase getTrainingSessionUseCase;

    public GetTrainingSessionController(GetTrainingSessionUseCase getTrainingSessionUseCase) {
        this.getTrainingSessionUseCase = getTrainingSessionUseCase;
    }

    @GetMapping("/session/{sessionId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training session found successfully"),
            @ApiResponse(responseCode = "404", description = "Training session not found")
    })
    public ResponseEntity<TrainingSessionWebModel> getTrainingSession(@PathVariable("sessionId") String sessionId) {
        TrainingSessionId trainingSessionId = TrainingSessionIdParser.parseTrainingSessionId(sessionId);
        TrainingSession trainingSession = getTrainingSessionUseCase.getTrainingSession(trainingSessionId);
        return ResponseEntity.ok(TrainingSessionWebModel.fromDomainModel(trainingSession));
    }
}
