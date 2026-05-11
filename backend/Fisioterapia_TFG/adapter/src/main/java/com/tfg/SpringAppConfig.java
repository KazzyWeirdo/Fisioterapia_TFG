package com.tfg;

import com.tfg.application.port.in.auditlog.GetAllAuditLogsUseCase;
import com.tfg.application.port.in.exercisetemplate.CreateExerciseTemplateUseCase;
import com.tfg.application.port.in.exercisetemplate.GetAllExerciseTemplatesUseCase;
import com.tfg.application.port.in.exercisetemplate.GetExerciseTemplateByIdUseCase;
import com.tfg.application.port.in.indiba.CreateIndibaSessionUseCase;
import com.tfg.application.port.in.indiba.GetAllIndibaSessionsForExportUseCase;
import com.tfg.application.port.in.indiba.GetIndibaSessionFromPatientUseCase;
import com.tfg.application.port.in.indiba.GetIndibaSessionUseCase;
import com.tfg.application.port.in.patient.CreatePatientUseCase;
import com.tfg.application.port.in.patient.DeletePatientUseCase;
import com.tfg.application.port.in.patient.DischargePatientUseCase;
import com.tfg.application.port.in.patient.GetAllPatientsForExportUseCase;
import com.tfg.application.port.in.patient.GetAllPatientsUseCase;
import com.tfg.application.port.in.patient.GetPatientUseCase;
import com.tfg.application.port.in.patient.UpdatePatientFunctionalScoreUseCase;
import com.tfg.application.port.in.patient.UpdatePatientUseCase;
import com.tfg.application.port.in.statistics.GetIndibaSessionStatsUseCase;
import com.tfg.application.port.in.statistics.GetPathologyRehabilitationStatsUseCase;
import com.tfg.application.port.in.physiotherapist.GetPhysiotherapistUseCase;
import com.tfg.application.port.in.physiotherapist.LogPhysiotherapistUseCase;
import com.tfg.application.port.in.physiotherapist.RequestPasswordResetUseCase;
import com.tfg.application.port.in.physiotherapist.ResetPasswordUseCase;
import com.tfg.application.port.in.pni.CreatePniReportUseCase;
import com.tfg.application.port.in.pni.GetAllPniReportsForExportUseCase;
import com.tfg.application.port.in.pni.GetPniReportUseCase;
import com.tfg.application.port.in.pni.GetPniReportsFromPatientUseCase;
import com.tfg.application.port.in.polar.ManagePolarConnectionUseCase;
import com.tfg.application.port.in.polar.SyncPolarDataUseCase;
import com.tfg.application.port.in.physiotherapist.RegisterPhysiotherapistUseCase;
import com.tfg.application.port.in.statistics.GetPatientTransitionRatioUseCase;
import com.tfg.application.port.in.statistics.GetWorkloadProgressionUseCase;
import com.tfg.application.port.in.trainingsession.CreateTrainingSessionUseCase;
import com.tfg.application.port.in.trainingsession.GetAllTrainingSessionsForExportUseCase;
import com.tfg.application.port.in.trainingsession.GetTrainingSessionByPatientUseCase;
import com.tfg.application.port.in.trainingsession.GetTrainingSessionUseCase;
import com.tfg.application.port.out.mail.EmailSenderPort;
import com.tfg.application.port.out.persistence.*;
import com.tfg.application.port.out.springsecurity.CredentialsValidatorPort;
import com.tfg.application.port.out.springsecurity.PasswordEncoderPort;
import com.tfg.application.port.out.springsecurity.TokenGeneratorPort;
import com.tfg.application.port.out.polar.PolarRepository;
import com.tfg.application.service.physiotherapist.LogPhysiotherapistService;
import com.tfg.application.service.trainingsession.CreateTrainingSessionService;
import com.tfg.application.service.trainingsession.GetTrainingSessionByPatientService;
import com.tfg.application.service.trainingsession.GetTrainingSessionService;
import com.tfg.application.service.auditlog.GetAllAuditLogsService;
import com.tfg.application.service.exercisetemplate.CreateExerciseTemplateService;
import com.tfg.application.service.exercisetemplate.GetAllExerciseTemplatesService;
import com.tfg.application.service.exercisetemplate.GetExerciseTemplateByIdService;
import com.tfg.application.service.indiba.CreateIndibaSessionService;
import com.tfg.application.service.indiba.GetAllIndibaSessionsForExportService;
import com.tfg.application.service.indiba.GetIndibaSessionFromPatientService;
import com.tfg.application.service.indiba.GetIndibaSessionService;
import com.tfg.application.service.patient.CreatePatientService;
import com.tfg.application.service.patient.DeletePatientService;
import com.tfg.application.service.patient.DischargePatientService;
import com.tfg.application.service.patient.GetAllPatientsForExportService;
import com.tfg.application.service.patient.GetAllPatientsService;
import com.tfg.application.service.patient.UpdatePatientFunctionalScoreService;
import com.tfg.application.service.patient.UpdatePatientService;
import com.tfg.application.service.statistics.GetIndibaSessionStatsService;
import com.tfg.application.service.statistics.GetPathologyRehabilitationStatsService;
import com.tfg.application.service.physiotherapist.GetPhysiotherapistService;
import com.tfg.application.service.physiotherapist.RequestPasswordResetService;
import com.tfg.application.service.physiotherapist.ResetPasswordService;
import com.tfg.application.service.pni.CreatePniReportService;
import com.tfg.application.service.pni.GetAllPniReportsForExportService;
import com.tfg.application.service.pni.GetPniReportService;
import com.tfg.application.service.pni.GetPniReportsFromPatientService;
import com.tfg.application.service.polar.ManagePolarConnectionService;
import com.tfg.application.service.polar.SyncPolarDataService;
import com.tfg.application.service.physiotherapist.RegisterPhysiotherapistService;
import com.tfg.application.service.statistics.GetPatientTransitionRatioService;
import com.tfg.application.service.statistics.GetWorkloadProgressionService;
import com.tfg.application.service.trainingsession.GetAllTrainingSessionsForExportService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.tfg.application.service.patient.GetPatientService;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

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
    ExerciseTemplateRepository exerciseTemplateRepository;

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
        return new CreateTrainingSessionService(trainingSessionRepository);
    }

    @Bean
    GetTrainingSessionUseCase getTrainingSessionUseCase() {
        return new GetTrainingSessionService(trainingSessionRepository);
    }

    @Bean
    GetTrainingSessionByPatientUseCase getTrainingSessionByPatientUseCase() {
        return new GetTrainingSessionByPatientService(trainingSessionRepository, patientRepository);
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
        return new LogPhysiotherapistService(tokenGeneratorPort, credentialsValidatorPort);
    }

    @Bean
    ResetPasswordUseCase resetPasswordUseCase() {
        return new ResetPasswordService(passwordResetTokenRepository, psychiatristRepository, passwordEncoderPort);
    }

    @Bean
    RequestPasswordResetUseCase requestPasswordResetUseCase() {
        return new RequestPasswordResetService(psychiatristRepository, passwordResetTokenRepository, emailSenderPort);
    }

    @Bean
    CreateExerciseTemplateUseCase createExerciseTemplateUseCase() {
        return new CreateExerciseTemplateService(exerciseTemplateRepository);
    }

    @Bean
    GetAllExerciseTemplatesUseCase getAllExerciseTemplatesUseCase() {
        return new GetAllExerciseTemplatesService(exerciseTemplateRepository);
    }

    @Bean
    GetExerciseTemplateByIdUseCase getExerciseTemplateByIdUseCase() {
        return new GetExerciseTemplateByIdService(exerciseTemplateRepository);
    }

    @Bean
    UpdatePatientFunctionalScoreUseCase updatePatientFunctionalScoreUseCase() {
        return new UpdatePatientFunctionalScoreService(patientRepository);
    }

    @Bean
    DischargePatientUseCase dischargePatientUseCase() {
        return new DischargePatientService(patientRepository);
    }

    @Bean
    DeletePatientUseCase deletePatientUseCase() {
        return new DeletePatientService(
                patientRepository,
                indibaSessionRepository,
                pniReportRepository,
                trainingSessionRepository);
    }

    @Bean
    GetIndibaSessionStatsUseCase getIndibaSessionStatsUseCase() {
        return new GetIndibaSessionStatsService(indibaSessionRepository);
    }

    @Bean
    GetPathologyRehabilitationStatsUseCase getPathologyRehabilitationStatsUseCase() {
        return new GetPathologyRehabilitationStatsService(patientRepository);
    }
}
