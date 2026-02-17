package com.tfg.adapter.in.rest.polar;

import com.tfg.adapter.in.rest.common.PatientIdParser;
import com.tfg.patient.PatientId;
import com.tfg.port.in.polar.ManagePolarConnectionUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/auth/polar")
@RequiredArgsConstructor
public class ManagePolarConnectionController {

    private final ManagePolarConnectionUseCase managePolarConnectionUseCase;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authorization succefully initiated"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/authorize")
    public RedirectView authorize(@RequestParam String patientId) {
        PatientId patientParsedId = PatientIdParser.parsePatientId(patientId);
        String url = managePolarConnectionUseCase.initiateConnection(patientParsedId);
        return new RedirectView(url);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Callback processed successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "409", description = "User already registered"),
            @ApiResponse(responseCode = "400", description = "Error registering user in Polar")
    })
    @GetMapping("/callback")
    public ResponseEntity<String> callback(
            @RequestParam String code,
            @RequestParam String state) {

        PatientId patientId = new PatientId(Integer.parseInt(state));

        managePolarConnectionUseCase.finalizeConnection(code, patientId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8")
                .body("¡Conexión con Polar Exitosa! Ya puedes cerrar esta ventana.");
    }
}
