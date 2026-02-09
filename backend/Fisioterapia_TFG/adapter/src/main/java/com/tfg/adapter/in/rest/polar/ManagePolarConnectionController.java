package com.tfg.adapter.in.rest.polar;

import com.tfg.adapter.in.rest.common.PatientIdParser;
import com.tfg.patient.PatientId;
import com.tfg.port.in.polar.ManagePolarConnectionUseCase;
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

    @GetMapping("/authorize")
    public RedirectView authorize(@RequestParam String patientId) {
        PatientId patientParsedId = PatientIdParser.parsePatientId(patientId);
        String url = managePolarConnectionUseCase.initiateConnection(patientParsedId);
        return new RedirectView(url);
    }

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
