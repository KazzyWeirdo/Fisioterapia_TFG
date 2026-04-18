package com.tfg.adapter.in.rest.patient;

import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.PatientSummaryElement;
import com.tfg.port.in.patient.GetAllPatientsUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class GetAllPatientsController {
    private final GetAllPatientsUseCase getAllPatientsUseCase;

    public GetAllPatientsController(GetAllPatientsUseCase getAllPatientsUseCase) {
        this.getAllPatientsUseCase = getAllPatientsUseCase;
    }

    @GetMapping("/list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patients found successfully"),
            @ApiResponse(responseCode = "204", description = "No Patients found")
    })
    public ResponseEntity<PagedResponse<PatientListWebModel>> getAllPatients(Pageable springPageable) {
        Sort.Order order = springPageable.getSort().getOrderFor("nameToUse");
        String sortDir = (order != null && order.isDescending()) ? "DESC" : "ASC";
        PageQuery query = new PageQuery(
                springPageable.getPageNumber(),
                springPageable.getPageSize(),
                "nameToUse",
                sortDir
        );

        PagedResponse<PatientSummaryElement> domainPagedResponse = getAllPatientsUseCase.getAllPatients(query);

        if (domainPagedResponse.content().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<PatientListWebModel> dtoContent = domainPagedResponse.content().stream()
                .map(patientSummaryElement -> new PatientListWebModel(
                        patientSummaryElement.id(),
                        patientSummaryElement.name(),
                        patientSummaryElement.surname()
                ))
                .toList();

        PagedResponse<PatientListWebModel> webResponse = new PagedResponse<>(
                dtoContent,
                domainPagedResponse.totalElements(),
                domainPagedResponse.totalPages(),
                domainPagedResponse.pageNumber(),
                domainPagedResponse.isLast()
        );

        return ResponseEntity.ok(webResponse);
    }
}
