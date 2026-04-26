package com.tfg.adapter.out.persistence.auditlog;

import com.tfg.auditlog.AuditLog;
import com.tfg.model.auditlog.AuditLogFactory;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.port.out.persistence.AuditLogRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import com.tfg.adapter.out.persistence.BaseRepositoryIT;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

public abstract class AbstractAuditLogRepositoryTest extends BaseRepositoryIT {
    private final AuditLog testAuditLog = AuditLogFactory.createTestAuditLog("Uno");
    private final AuditLog testAuditLog2 = AuditLogFactory.createTestAuditLog("Dos");

    @Autowired
    public AuditLogRepository auditLogRepository;

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

    @Test
    public void givenPageQuery_whenFindAll_thenReturnPaginatedAuditLogs() {
        auditLogRepository.save(testAuditLog);
        auditLogRepository.save(testAuditLog2);

        PageQuery query = new PageQuery(0, 10);

        PagedResponse<AuditLog> response = auditLogRepository.findAll(query);

        List<AuditLog> auditLogs = response.content();

        assertThat(response.totalElements()).isEqualTo(2);
        assertThat(response.totalPages()).isEqualTo(1);
        assertThat(response.pageNumber()).isEqualTo(0);
        assertThat(response.isLast()).isTrue();

        assertThat(auditLogs).hasSize(2);
        assertThat(auditLogs.get(0).getId()).isEqualTo(testAuditLog.getId());
        assertThat(auditLogs.get(0).getAction()).isEqualTo(testAuditLog.getAction());
        assertThat(auditLogs.get(1).getId()).isEqualTo(testAuditLog2.getId());
        assertThat(auditLogs.get(1).getAction()).isEqualTo(testAuditLog2.getAction());
    }
}
