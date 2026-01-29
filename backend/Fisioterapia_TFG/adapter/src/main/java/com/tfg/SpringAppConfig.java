package com.tfg;

import com.tfg.port.in.indiba.CreateIndibaSessionUseCase;
import com.tfg.port.in.indiba.GetIndibaSessionFromPatientUseCase;
import com.tfg.port.in.indiba.GetIndibaSessionUseCase;
import com.tfg.port.in.patient.CreatePatientUseCase;
import com.tfg.port.in.patient.GetPatientUseCase;
import com.tfg.port.in.patient.UpdatePatientUseCase;
import com.tfg.port.out.persistence.IndibaSessionRepository;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.service.indiba.CreateIndibaSessionService;
import com.tfg.service.indiba.GetIndibaSessionFromPatientService;
import com.tfg.service.indiba.GetIndibaSessionService;
import com.tfg.service.patient.CreatePatientService;
import com.tfg.service.patient.UpdatePatientService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.tfg.service.patient.GetPatientService;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title= "fisioterapia API",
version = "1.0",
license = @License(name = "Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International Public License", url = "https://creativecommons.org/licenses/by-nc-sa/4.0/")))

public class SpringAppConfig {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    IndibaSessionRepository indibaSessionRepository;

    @Bean
    GetPatientUseCase getPatientUseCase() {
        return new GetPatientService(patientRepository);
    }

    @Bean
    CreatePatientUseCase createPatientUseCase() {
        return new CreatePatientService(patientRepository);
    }

    @Bean
    UpdatePatientUseCase updatePatientUseCase() {
        return new UpdatePatientService(patientRepository);
    }

    @Bean
    CreateIndibaSessionUseCase createIndibaSessionUseCase() {
        return new CreateIndibaSessionService(indibaSessionRepository);
    }

    @Bean
    GetIndibaSessionUseCase getIndibaSessionUseCase() {
        return new GetIndibaSessionService(indibaSessionRepository);
    }

    @Bean
    GetIndibaSessionFromPatientUseCase getIndibaSessionFromPatientUseCase() {
        return new GetIndibaSessionFromPatientService(indibaSessionRepository, patientRepository);
    }
}
