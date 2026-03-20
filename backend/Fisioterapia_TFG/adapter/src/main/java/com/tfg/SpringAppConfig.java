package com.tfg;

import com.tfg.port.in.indiba.CreateIndibaSessionUseCase;
import com.tfg.port.in.indiba.GetIndibaSessionFromPatientUseCase;
import com.tfg.port.in.indiba.GetIndibaSessionUseCase;
import com.tfg.port.in.patient.CreatePatientUseCase;
import com.tfg.port.in.patient.GetPatientUseCase;
import com.tfg.port.in.patient.UpdatePatientUseCase;
import com.tfg.port.in.pni.CreatePniReportUseCase;
import com.tfg.port.in.pni.GetPniReportUseCase;
import com.tfg.port.in.pni.GetPniReportsFromPatientUseCase;
import com.tfg.port.in.polar.ManagePolarConnectionUseCase;
import com.tfg.port.in.polar.SyncPolarDataUseCase;
import com.tfg.port.in.statistics.GetPatientTransitionRatioUseCase;
import com.tfg.port.in.statistics.GetWorkloadProgressionUseCase;
import com.tfg.port.in.trainingsession.CreateTrainingSessionUseCase;
import com.tfg.port.in.trainingsession.GetTrainingSessionByPatientUseCase;
import com.tfg.port.in.trainingsession.GetTrainingSessionUseCase;
import com.tfg.port.out.persistence.IndibaSessionRepository;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.PniReportRepository;
import com.tfg.port.out.persistence.TrainingSessionRepository;
import com.tfg.port.out.polar.PolarRepository;
import com.tfg.service.indiba.CreateIndibaSessionService;
import com.tfg.service.indiba.GetIndibaSessionFromPatientService;
import com.tfg.service.indiba.GetIndibaSessionService;
import com.tfg.service.patient.CreatePatientService;
import com.tfg.service.patient.UpdatePatientService;
import com.tfg.service.pni.CreatePniReportService;
import com.tfg.service.pni.GetPniReportService;
import com.tfg.service.pni.GetPniReportsFromPatientService;
import com.tfg.service.polar.ManagePolarConnectionService;
import com.tfg.service.polar.SyncPolarDataService;
import com.tfg.service.statistics.GetPatientTransitionRatioService;
import com.tfg.service.statistics.GetWorkloadProgressionService;
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

    @Autowired
    PniReportRepository pniReportRepository;

    @Autowired
    PolarRepository polarRepository;

    @Autowired
    TrainingSessionRepository trainingSessionRepository;

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

    @Bean
    GetPniReportUseCase getPniReportUseCase() {
        return new GetPniReportService(pniReportRepository);
    }

    @Bean
    GetPniReportsFromPatientUseCase getPniReportsFromPatientUseCase() {
        return new GetPniReportsFromPatientService(pniReportRepository, patientRepository);
    }

    @Bean
    CreatePniReportUseCase createPniReportUseCase() {
        return new CreatePniReportService(pniReportRepository);
    }

    @Bean
    ManagePolarConnectionUseCase managePolarConnectionUseCase() { return new ManagePolarConnectionService(polarRepository, patientRepository);}

    @Bean
    SyncPolarDataUseCase syncPolarDataUseCase() { return new SyncPolarDataService(patientRepository, pniReportRepository, polarRepository);}

    @Bean
    CreateTrainingSessionUseCase createTrainingSessionUseCase() {
        return new com.tfg.service.trainingsession.CreateTrainingSessionService(trainingSessionRepository);
    }

    @Bean
    GetTrainingSessionUseCase getTrainingSessionUseCase() {
        return new com.tfg.service.trainingsession.GetTrainingSessionService(trainingSessionRepository);
    }

    @Bean
    GetTrainingSessionByPatientUseCase getTrainingSessionByPatientUseCase() {
        return new com.tfg.service.trainingsession.GetTrainingSessionByPatientService(trainingSessionRepository, patientRepository);
    }

    @Bean
    GetPatientTransitionRatioUseCase getPatientTransitionRatioUseCase() {
        return new GetPatientTransitionRatioService(patientRepository, indibaSessionRepository, trainingSessionRepository);
    }

    @Bean
    GetWorkloadProgressionUseCase getWorkloadProgressionUseCase() {
        return new GetWorkloadProgressionService(trainingSessionRepository, patientRepository);
    }
}
