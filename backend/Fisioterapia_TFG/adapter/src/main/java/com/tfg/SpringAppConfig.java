package com.tfg;

import com.tfg.port.in.auditlog.GetAllAuditLogsUseCase;
import com.tfg.port.in.indiba.CreateIndibaSessionUseCase;
import com.tfg.port.in.indiba.GetAllIndibaSessionsForExportUseCase;
import com.tfg.port.in.indiba.GetIndibaSessionFromPatientUseCase;
import com.tfg.port.in.indiba.GetIndibaSessionUseCase;
import com.tfg.port.in.patient.CreatePatientUseCase;
import com.tfg.port.in.patient.GetAllPatientsForExportUseCase;
import com.tfg.port.in.patient.GetAllPatientsUseCase;
import com.tfg.port.in.patient.GetPatientUseCase;
import com.tfg.port.in.patient.UpdatePatientUseCase;
import com.tfg.port.in.physiotherapist.GetPhysiotherapistUseCase;
import com.tfg.port.in.physiotherapist.LogPhysiotherapistUseCase;
import com.tfg.port.in.physiotherapist.RequestPasswordResetUseCase;
import com.tfg.port.in.physiotherapist.ResetPasswordUseCase;
import com.tfg.port.in.pni.CreatePniReportUseCase;
import com.tfg.port.in.pni.GetAllPniReportsForExportUseCase;
import com.tfg.port.in.pni.GetPniReportUseCase;
import com.tfg.port.in.pni.GetPniReportsFromPatientUseCase;
import com.tfg.port.in.polar.ManagePolarConnectionUseCase;
import com.tfg.port.in.polar.SyncPolarDataUseCase;
import com.tfg.port.in.physiotherapist.RegisterPhysiotherapistUseCase;
import com.tfg.port.in.statistics.GetPatientTransitionRatioUseCase;
import com.tfg.port.in.statistics.GetWorkloadProgressionUseCase;
import com.tfg.port.in.trainingsession.CreateTrainingSessionUseCase;
import com.tfg.port.in.trainingsession.GetAllTrainingSessionsForExportUseCase;
import com.tfg.port.in.trainingsession.GetTrainingSessionByPatientUseCase;
import com.tfg.port.in.trainingsession.GetTrainingSessionUseCase;
import com.tfg.port.out.mail.EmailSenderPort;
import com.tfg.port.out.springsecurity.CredentialsValidatorPort;
import com.tfg.port.out.springsecurity.PasswordEncoderPort;
import com.tfg.port.out.springsecurity.TokenGeneratorPort;
import com.tfg.port.out.persistence.*;
import com.tfg.port.out.polar.PolarRepository;
import com.tfg.service.auditlog.GetAllAuditLogsService;
import com.tfg.service.indiba.CreateIndibaSessionService;
import com.tfg.service.indiba.GetAllIndibaSessionsForExportService;
import com.tfg.service.indiba.GetIndibaSessionFromPatientService;
import com.tfg.service.indiba.GetIndibaSessionService;
import com.tfg.service.patient.CreatePatientService;
import com.tfg.service.patient.GetAllPatientsForExportService;
import com.tfg.service.patient.GetAllPatientsService;
import com.tfg.service.patient.UpdatePatientService;
import com.tfg.service.physiotherapist.GetPhysiotherapistService;
import com.tfg.service.physiotherapist.RequestPasswordResetService;
import com.tfg.service.physiotherapist.ResetPasswordService;
import com.tfg.service.pni.CreatePniReportService;
import com.tfg.service.pni.GetAllPniReportsForExportService;
import com.tfg.service.pni.GetPniReportService;
import com.tfg.service.pni.GetPniReportsFromPatientService;
import com.tfg.service.polar.ManagePolarConnectionService;
import com.tfg.service.polar.SyncPolarDataService;
import com.tfg.service.physiotherapist.RegisterPhysiotherapistService;
import com.tfg.service.statistics.GetPatientTransitionRatioService;
import com.tfg.service.statistics.GetWorkloadProgressionService;
import com.tfg.service.trainingsession.GetAllTrainingSessionsForExportService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.tfg.service.patient.GetPatientService;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootApplication
@EnableAspectJAutoProxy
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

    @Autowired
    PhysiotherapistRepository psychiatristRepository;

    @Autowired
    AuditLogRepository auditLogRepository;

    @Autowired
    TokenGeneratorPort tokenGeneratorPort;

    @Autowired
    EmailSenderPort emailSenderPort;

    @Autowired
    CredentialsValidatorPort credentialsValidatorPort;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    PasswordEncoderPort passwordEncoderPort;

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
    GetAllPatientsUseCase getAllPatientsUseCase() {return new GetAllPatientsService(patientRepository);}

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

    @Bean
    GetAllPatientsForExportUseCase getAllPatientsForExportUseCase() {
        return new GetAllPatientsForExportService(patientRepository);
    }

    @Bean
    GetAllIndibaSessionsForExportUseCase getAllIndibaSessionsForExportUseCase() {
        return new GetAllIndibaSessionsForExportService(indibaSessionRepository);
    }

    @Bean
    GetAllPniReportsForExportUseCase getAllPniReportsForExportUseCase() {
        return new GetAllPniReportsForExportService(pniReportRepository);
    }

    @Bean
    GetAllTrainingSessionsForExportUseCase getAllTrainingSessionsForExportUseCase() {
        return new GetAllTrainingSessionsForExportService(trainingSessionRepository);
    }

    @Bean
    GetAllAuditLogsUseCase getAllAuditLogsUseCase() {
        return new GetAllAuditLogsService(auditLogRepository);
    }

    @Bean
    GetPhysiotherapistUseCase getPhysiotherapistUseCase() {
        return new GetPhysiotherapistService(psychiatristRepository);
    }

    @Bean
    RegisterPhysiotherapistUseCase registerPsychiatristUseCase() {
        return new RegisterPhysiotherapistService(psychiatristRepository, passwordEncoderPort, requestPasswordResetUseCase());
    }

    @Bean
    LogPhysiotherapistUseCase logPhysiotherapistUseCase() {
        return new com.tfg.service.physiotherapist.LogPhysiotherapistService(tokenGeneratorPort, credentialsValidatorPort);
    }

    @Bean
    ResetPasswordUseCase resetPasswordUseCase() {
        return new ResetPasswordService(passwordResetTokenRepository, psychiatristRepository, passwordEncoderPort);
    }

    @Bean
    RequestPasswordResetUseCase requestPasswordResetUseCase() {
        return new RequestPasswordResetService(psychiatristRepository, passwordResetTokenRepository, emailSenderPort);
    }
}
