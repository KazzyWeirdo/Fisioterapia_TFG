package com.tfg.adapter.in.rest.indiba;

import com.tfg.indiba.IndibaSession;
import com.tfg.port.in.indiba.GetAllIndibaSessionsForExportUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/indiba")
public class GetAllIndibaSessionsForExportController {

    private final GetAllIndibaSessionsForExportUseCase getAllIndibaSessionsForExportUseCase;

    public GetAllIndibaSessionsForExportController(GetAllIndibaSessionsForExportUseCase getAllIndibaSessionsForExportUseCase) {
        this.getAllIndibaSessionsForExportUseCase = getAllIndibaSessionsForExportUseCase;
    }

    @GetMapping("/export")
    public ResponseEntity<List<IndibaExportWebModel>> exportIndibaSessions() {
        List<IndibaSession> sessions = getAllIndibaSessionsForExportUseCase.getAllIndibaSessionsForExport();
        if (sessions.isEmpty()) return ResponseEntity.noContent().build();
        List<IndibaExportWebModel> dto = sessions.stream().map(s -> new IndibaExportWebModel(
                s.getPatient().getId().value(),
                s.getId().value(),
                s.getBeginSession().toInstant().toString(),
                s.getEndSession().toInstant().toString(),
                s.getTreatedArea(),
                s.getMode().name(),
                s.getIntensity(),
                s.getObjective(),
                s.getObservations()
        )).toList();
        return ResponseEntity.ok(dto);
    }
}
