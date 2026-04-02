package com.tfg.adapter.out.persistence.auditlog;

import com.tfg.auditlog.AuditLog;
import com.tfg.model.auditlog.AuditLogFactory;
import com.tfg.port.out.persistence.AuditLogRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractAuditLogRepositoryTest {
    private AuditLog testAuditLog;

    @Autowired
    public AuditLogRepository auditLogRepository;

    @BeforeEach
    void setUp() {
        testAuditLog = AuditLogFactory.createTestAuditLog("Uno");
    }

    @AfterEach
    void tearDown() {
        auditLogRepository.deleteAll();
    }

    @Test
    public void givenAuditLog_whenSave_thenSaved() {
        auditLogRepository.save(testAuditLog);
    }

    @Test
    public void givenAuditLog_whenDeleteAll_thenDeleted() {
        auditLogRepository.save(testAuditLog);
        auditLogRepository.deleteAll();
    }
}
