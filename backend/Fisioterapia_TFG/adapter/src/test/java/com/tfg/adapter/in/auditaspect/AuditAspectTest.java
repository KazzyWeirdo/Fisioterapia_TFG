package com.tfg.adapter.in.auditaspect;

import com.tfg.auditlog.AuditLog;
import com.tfg.indiba.IndibaSession;
import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.pni.PniReport;
import com.tfg.port.out.persistence.AuditLogRepository;
import com.tfg.trainingsession.TrainingSession;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditAspectTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private JoinPoint joinPoint;

    @InjectMocks
    private AuditAspect auditAspect;

    @Captor
    private ArgumentCaptor<AuditLog> auditLogCaptor;

    private void mockSecurityContext() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("admin_user");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void givenNonDomainEntity_whenLogAfterSave_thenNoInteractionWithRepository() {
        Object nonDomainEntity = "Any Entity";
        when(joinPoint.getArgs()).thenReturn(new Object[]{nonDomainEntity});

        auditAspect.logAfterSave(joinPoint);

        verify(auditLogRepository, never()).save(any());
    }

    @Test
    void givenPhysiotherapist_whenLogAfterSave_thenSaveEmailInDetails() {
        mockSecurityContext();
        Physiotherapist physiotherapist = mock(Physiotherapist.class, RETURNS_DEEP_STUBS);
        when(physiotherapist.getEmail().value()).thenReturn("fisio@hospital.com");

        when(joinPoint.getArgs()).thenReturn(new Object[]{physiotherapist});

        auditAspect.logAfterSave(joinPoint);

        verify(auditLogRepository, times(1)).save(auditLogCaptor.capture());
        AuditLog savedLog = auditLogCaptor.getValue();

        assertEquals("CREATE", savedLog.getAction());
        assertEquals("fisio@hospital.com", savedLog.getDetails());
        assertEquals("admin_user", savedLog.getUser());
    }

    @Test
    void givenPatient_whenLogAfterSave_thenSaveNameInDetails() {
        mockSecurityContext();
        Patient patient = mock(Patient.class);
        when(patient.getLegalName()).thenReturn("John");
        when(patient.getSurname()).thenReturn("Doe");

        when(joinPoint.getArgs()).thenReturn(new Object[]{patient});

        auditAspect.logAfterSave(joinPoint);

        verify(auditLogRepository, times(1)).save(auditLogCaptor.capture());
        AuditLog savedLog = auditLogCaptor.getValue();

        assertEquals("CREATE", savedLog.getAction());
        assertEquals("John Doe", savedLog.getDetails());
        assertEquals("admin_user", savedLog.getUser());
    }

    @Test
    void givenIndibaSession_whenLogAfterSave_thenSavePatientAndAreaInDetails() {
        mockSecurityContext();
        Patient patient = mock(Patient.class);
        when(patient.getLegalName()).thenReturn("Jane");
        when(patient.getSurname()).thenReturn("Smith");

        IndibaSession session = mock(IndibaSession.class);
        when(session.getPatient()).thenReturn(patient);
        when(session.getTreatedArea()).thenReturn("Knee");

        when(joinPoint.getArgs()).thenReturn(new Object[]{session});

        auditAspect.logAfterSave(joinPoint);

        verify(auditLogRepository, times(1)).save(auditLogCaptor.capture());
        AuditLog savedLog = auditLogCaptor.getValue();

        assertEquals("CREATE", savedLog.getAction());
        assertEquals("Patient: Jane Smith | Area: Knee", savedLog.getDetails());
    }

    @Test
    void givenPniReport_whenLogAfterSave_thenSavePatientNameInDetails() {
        mockSecurityContext();
        Patient patient = mock(Patient.class);
        when(patient.getLegalName()).thenReturn("Ana");
        when(patient.getSurname()).thenReturn("Garcia");

        PniReport report = mock(PniReport.class);
        when(report.getPatient()).thenReturn(patient);

        when(joinPoint.getArgs()).thenReturn(new Object[]{report});

        auditAspect.logAfterSave(joinPoint);

        verify(auditLogRepository, times(1)).save(auditLogCaptor.capture());
        AuditLog savedLog = auditLogCaptor.getValue();

        assertEquals("CREATE", savedLog.getAction());
        assertEquals("Patient: Ana Garcia", savedLog.getDetails());
    }

    @Test
    void givenTrainingSession_whenLogAfterSave_thenSavePatientNameInDetails() {
        mockSecurityContext();
        Patient patient = mock(Patient.class);
        when(patient.getLegalName()).thenReturn("Carlos");
        when(patient.getSurname()).thenReturn("Lopez");

        TrainingSession session = mock(TrainingSession.class);
        when(session.getPatient()).thenReturn(patient);

        when(joinPoint.getArgs()).thenReturn(new Object[]{session});

        auditAspect.logAfterSave(joinPoint);

        verify(auditLogRepository, times(1)).save(auditLogCaptor.capture());
        AuditLog savedLog = auditLogCaptor.getValue();

        assertEquals("CREATE", savedLog.getAction());
        assertEquals("Patient: Carlos Lopez", savedLog.getDetails());
    }

    @Test
    void givenPatient_whenLogAfterUpdate_thenSaveUpdateAuditLog() {
        mockSecurityContext();
        PatientId patientId = mock(PatientId.class);
        Patient patient = mock(Patient.class);
        when(patient.getLegalName()).thenReturn("Maria");
        when(patient.getSurname()).thenReturn("Torres");

        when(joinPoint.getArgs()).thenReturn(new Object[]{patientId, patient});

        auditAspect.logAfterUpdate(joinPoint);

        verify(auditLogRepository, times(1)).save(auditLogCaptor.capture());
        AuditLog savedLog = auditLogCaptor.getValue();

        assertEquals("UPDATE", savedLog.getAction());
        assertEquals("Maria Torres", savedLog.getDetails());
        assertEquals("admin_user", savedLog.getUser());
    }

    @Test
    void givenEmptyArgs_whenLogAfterSave_thenReturnEarly() {
        when(joinPoint.getArgs()).thenReturn(new Object[]{});

        auditAspect.logAfterSave(joinPoint);

        verify(auditLogRepository, never()).save(any());
    }

    @Test
    void givenNullArgs_whenLogAfterSave_thenReturnEarly() {
        when(joinPoint.getArgs()).thenReturn(null);

        auditAspect.logAfterSave(joinPoint);

        verify(auditLogRepository, never()).save(any());
    }

    @Test
    void givenSingleArg_whenLogAfterUpdate_thenReturnEarly() {
        when(joinPoint.getArgs()).thenReturn(new Object[]{mock(PatientId.class)});

        auditAspect.logAfterUpdate(joinPoint);

        verify(auditLogRepository, never()).save(any());
    }
}
