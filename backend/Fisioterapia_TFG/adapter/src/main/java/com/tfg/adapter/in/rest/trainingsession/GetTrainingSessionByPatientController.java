package com.tfg.adapter.in.rest.trainingsession;

import com.tfg.adapter.in.rest.common.PatientIdParser;
import com.tfg.adapter.in.rest.pni.PniReportListWebModel;
import com.tfg.patient.PatientId;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.PniReportSummaryElement;
import com.tfg.pojos.query.TrainingSessionSummaryElement;
import com.tfg.port.in.trainingsession.GetTrainingSessionByPatientUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/training-session")
public class GetTrainingSessionByPatientController {

    private final GetTrainingSessionByPatientUseCase getTrainingSessionByPatientUseCase;

    public GetTrainingSessionByPatientController(GetTrainingSessionByPatientUseCase getTrainingSessionByPatientUseCase) {
        this.getTrainingSessionByPatientUseCase = getTrainingSessionByPatientUseCase;
    }

    @GetMapping("/{patientId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training sessions found successfully"),
            @ApiResponse(responseCode = "204", description = "No training sessions found for the patient"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<PagedResponse<TrainingSessionListWebModel>> getTrainingSessionsByPatient(@PathVariable("patientId") String grabbedPatientId, Pageable springPageable) {
        PatientId patientId = PatientIdParser.parsePatientId(grabbedPatientId);
        PageQuery query = new PageQuery(springPageable.getPageNumber(), springPageable.getPageSize());
        PagedResponse<TrainingSessionSummaryElement> domainPagedResponse = getTrainingSessionByPatientUseCase.getTrainingSessionFromPatient(query, patientId);

        if (domainPagedResponse.content().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<TrainingSessionListWebModel> dtoContent = domainPagedResponse.content().stream()
                .map(trainingSessionSummaryElement -> new TrainingSessionListWebModel(
                        trainingSessionSummaryElement.id(),
                        trainingSessionSummaryElement.date()
                ))
                .toList();

        PagedResponse<TrainingSessionListWebModel> webResponse = new PagedResponse<>(
                dtoContent,
                domainPagedResponse.totalElements(),
                domainPagedResponse.totalPages(),
                domainPagedResponse.pageNumber(),
                domainPagedResponse.isLast()
        );

        return ResponseEntity.ok(webResponse);
    }
}
