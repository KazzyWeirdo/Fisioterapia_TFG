package com.tfg;

import com.tfg.port.in.patient.GetPatientUseCase;
import com.tfg.port.out.persistence.PatientRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title= "fisioterapia API",
version = "1.0",
license = @License(name = "Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International Public License", url = "https://creativecommons.org/licenses/by-nc-sa/4.0/")))

public class SpringAppConfig {

    @Autowired
    PatientRepository patientRepository;
}
