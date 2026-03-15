package com.tfg.adapter.in.rest.statistics;

import com.tfg.adapter.in.rest.common.PatientIdParser;
import com.tfg.patient.PatientId;
import com.tfg.port.in.statistics.GetPatientTransitionRatioUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics/")
public class GetPatientTransitionRatioController {

    private final GetPatientTransitionRatioUseCase getPatientTransitionRatioUseCase;

    public GetPatientTransitionRatioController(GetPatientTransitionRatioUseCase getPatientTransitionRatioUseCase) {
        this.getPatientTransitionRatioUseCase = getPatientTransitionRatioUseCase;
    }

    @GetMapping("/{patientId}/{year}/patient-transition-ratio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transition ratio of year found successfully"),
            @ApiResponse(responseCode = "204", description = "Transition ratio of year is empty"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<List<TransitionRatioWebModel>> getPatientTransitionRatio(@PathVariable("patientId")String grabbedPatientId,
                                                                             @PathVariable("year") int year) {
        PatientId patientId = PatientIdParser.parsePatientId(grabbedPatientId);
        List<TransitionRatioWebModel> ratio = getPatientTransitionRatioUseCase.getTransitionRatio(patientId, year)
                .stream()
                .map(TransitionRatioWebModel::fromDomainModel)
                .toList();
        if(ratio.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(ratio);
    }
}
