package com.tfg.adapter.in.rest.indiba;

import com.tfg.adapter.in.rest.common.IndibaIdParser;
import com.tfg.indiba.IndibaSession;
import com.tfg.indiba.IndibaSessionId;
import com.tfg.port.in.indiba.GetIndibaSessionUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/indiba")
public class GetIndibaSessionController {

    private final GetIndibaSessionUseCase getIndibaSessionUseCase;

    public GetIndibaSessionController(GetIndibaSessionUseCase getIndibaSessionUseCase) {
        this.getIndibaSessionUseCase = getIndibaSessionUseCase;
    }

    @GetMapping("/{sessionId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indiba session found successfully"),
            @ApiResponse(responseCode = "404", description = "Indiba session not found")
    })
    public ResponseEntity<IndibaWebModel> getIndibaSession(@PathVariable("sessionId") String sessionIdString) {
        IndibaSessionId indibaSessionId = IndibaIdParser.parseIndibaId(sessionIdString);
        IndibaSession indibaSession = getIndibaSessionUseCase.getIndibaSession(indibaSessionId);
        return ResponseEntity.ok(IndibaWebModel.fromDomainModel(indibaSession));
    }
}
