package com.tfg.adapter.in.auditaspect;

import com.tfg.auditlog.AuditLog;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.port.out.persistence.AuditLogRepository;
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
    void givenGenericEntity_whenLogAfterSave_thenSaveGenericAuditLog() {
        mockSecurityContext();
        Object genericEntity = "Any Entity";
        when(joinPoint.getArgs()).thenReturn(new Object[]{genericEntity});

        auditAspect.logAfterSave(joinPoint);

        verify(auditLogRepository, times(1)).save(auditLogCaptor.capture());
        AuditLog savedLog = auditLogCaptor.getValue();

        assertEquals("CREATE/UPDATE", savedLog.getAction());
        assertEquals("String", savedLog.getEntityName());
        assertEquals("Entity Saved", savedLog.getDetails());
        assertEquals("admin_user", savedLog.getUser());
        assertNotNull(savedLog.getId());
        assertNotNull(savedLog.getTimestamp());
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

        assertEquals("fisio@hospital.com", savedLog.getDetails());
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
}