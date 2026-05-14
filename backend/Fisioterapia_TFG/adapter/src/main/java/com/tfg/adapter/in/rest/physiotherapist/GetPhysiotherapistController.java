package com.tfg.adapter.in.rest.physiotherapist;

import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.physiotherapist.PhysiotherapistId;
import com.tfg.port.in.physiotherapist.GetPhysiotherapistUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/physiotherapist")
public class GetPhysiotherapistController {

    private final GetPhysiotherapistUseCase getPhysiotherapistUseCase;

    public GetPhysiotherapistController(GetPhysiotherapistUseCase getPhysiotherapistUseCase) {
        this.getPhysiotherapistUseCase = getPhysiotherapistUseCase;
    }

    @GetMapping("/{physiotherapistId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Physiotherapist found successfully"),
            @ApiResponse(responseCode = "404", description = "Physiotherapist not found")
    })
    public ResponseEntity<PhysiotherapistWebModel> getPhysiotherapist(
            @PathVariable("physiotherapistId") int physiotherapistId) {
        Physiotherapist physio = getPhysiotherapistUseCase.getPhysiotherapist(new PhysiotherapistId(physiotherapistId));
        return ResponseEntity.ok(PhysiotherapistWebModel.fromDomainModel(physio));
    }
}
