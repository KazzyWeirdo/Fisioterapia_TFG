package com.tfg.adapter.in.rest.patient;

import com.tfg.patient.Patient;
import com.tfg.port.in.patient.GetAllPatientsForExportUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class GetAllPatientsForExportController {

    private final GetAllPatientsForExportUseCase getAllPatientsForExportUseCase;

    public GetAllPatientsForExportController(GetAllPatientsForExportUseCase getAllPatientsForExportUseCase) {
        this.getAllPatientsForExportUseCase = getAllPatientsForExportUseCase;
    }

    @GetMapping("/export")
    public ResponseEntity<List<PatientExportWebModel>> exportPatients() {
        List<Patient> patients = getAllPatientsForExportUseCase.getAllPatientsForExport();
        if (patients.isEmpty()) return ResponseEntity.noContent().build();
        List<PatientExportWebModel> dto = patients.stream().map(p -> new PatientExportWebModel(
                p.getId().value(),
                p.getDateOfBirth().toString(),
                p.getClinicalUseSex().name()
        )).toList();
        return ResponseEntity.ok(dto);
    }
}
