package com.tfg.adapter.in.rest.trainingsession;

import com.tfg.application.port.in.trainingsession.GetAllTrainingSessionsForExportUseCase;
import com.tfg.model.trainingsession.TrainingSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/training-session")
public class GetAllTrainingSessionsForExportController {

    private final GetAllTrainingSessionsForExportUseCase getAllTrainingSessionsForExportUseCase;

    public GetAllTrainingSessionsForExportController(GetAllTrainingSessionsForExportUseCase getAllTrainingSessionsForExportUseCase) {
        this.getAllTrainingSessionsForExportUseCase = getAllTrainingSessionsForExportUseCase;
    }

    @GetMapping("/export")
    public ResponseEntity<List<TrainingSetExportWebModel>> exportTrainingSessions() {
        List<TrainingSession> sessions = getAllTrainingSessionsForExportUseCase.getAllTrainingSessionsForExport();
        if (sessions.isEmpty()) return ResponseEntity.noContent().build();
        List<TrainingSetExportWebModel> dto = sessions.stream()
                .flatMap(session -> session.getExerciseTemplates().stream()
                        .flatMap(template -> template.getExercises().stream()
                                .flatMap(exercise -> exercise.getSets().stream()
                                        .map(set -> new TrainingSetExportWebModel(
                                                session.getPatient().getId().value(),
                                                session.getId().value(),
                                                session.getStartDateTime().toString(),
                                                template.getName(),
                                                exercise.getName(),
                                                set.setNumber(),
                                                set.weightKg(),
                                                set.reps(),
                                                set.restTimeSeconds(),
                                                set.rpe()
                                        ))
                                )
                        )
                ).toList();
        return ResponseEntity.ok(dto);
    }
}
