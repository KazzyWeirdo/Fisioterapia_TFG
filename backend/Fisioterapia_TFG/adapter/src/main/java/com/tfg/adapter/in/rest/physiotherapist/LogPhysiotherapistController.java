package com.tfg.adapter.in.rest.physiotherapist;

import com.tfg.port.in.physiotherapist.LogPhysiotherapistUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/physiotherapist")
public class LogPhysiotherapistController {

    private final LogPhysiotherapistUseCase logPhysiotherapistUseCase;
    @Value("${application.security.jwt.token-prefix}")
    private String tokenPrefix;


    public LogPhysiotherapistController(LogPhysiotherapistUseCase logPhysiotherapistUseCase) {
        this.logPhysiotherapistUseCase = logPhysiotherapistUseCase;
    }

    @PostMapping("/login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Psychiatrist logged in successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        String token = logPhysiotherapistUseCase.authenticate(
                request.physioId(),
                request.password()
        );

        AuthenticationResponse response = new AuthenticationResponse();
        response.setAccessToken(token);

        return ResponseEntity.ok()
                .header("Authorization", tokenPrefix + response.getAccessToken())
                .body(response);
    }
}
