package com.tfg.model.auditlog;

import com.tfg.auditlog.AuditLog;
import org.junit.jupiter.api.Test;

public class AuditLogTest {

    private static final AuditLog TEST_AUDIT_LOG = AuditLogFactory.createTestAuditLog();

    @Test
    public void givenValidValues_newAuditLog_succeeds() {
        AuditLog auditLog = TEST_AUDIT_LOG;

        assert auditLog.getId().equals(TEST_AUDIT_LOG.getId());
        assert auditLog.getEntityName().equals(TEST_AUDIT_LOG.getEntityName());
        assert auditLog.getAction().equals(TEST_AUDIT_LOG.getAction());
        assert auditLog.getTimestamp().equals(TEST_AUDIT_LOG.getTimestamp());
        assert auditLog.getDetails().equals(TEST_AUDIT_LOG.getDetails());
    }
}
