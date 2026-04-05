package application.auditlog;

import com.tfg.auditlog.AuditLog;
import com.tfg.model.auditlog.AuditLogFactory;
import com.tfg.pojos.auditlog.PageQuery;
import com.tfg.pojos.auditlog.PagedResponse;
import com.tfg.port.out.persistence.AuditLogRepository;
import com.tfg.service.auditlog.GetAllAuditLogsService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GetAllAuditLogsServiceTest {

    private final AuditLogRepository auditLogRepository = mock(AuditLogRepository.class);
    private final GetAllAuditLogsService getAllAuditLogsService = new GetAllAuditLogsService(auditLogRepository);

    private static final AuditLog TEST_AUDIT_LOG_1 = AuditLogFactory.createTestAuditLog("Log 1");
    private static final AuditLog TEST_AUDIT_LOG_2 = AuditLogFactory.createTestAuditLog("Log 2");

    @Test
    public void givenValidPageQuery_whenGetAllAuditLogs_thenReturnPagedResponse() {
        PageQuery query = new PageQuery(0, 2);

        PagedResponse<AuditLog> mockedResponse = new PagedResponse<>(
                List.of(TEST_AUDIT_LOG_1, TEST_AUDIT_LOG_2), // content
                2L,                                          // totalElements
                1,                                           // totalPages
                0,                                           // pageNumber
                true                                         // isLast
        );

        when(auditLogRepository.findAll(query)).thenReturn(mockedResponse);

        PagedResponse<AuditLog> result = getAllAuditLogsService.getAllAuditLogs(query);

        assertEquals(2, result.totalElements());
        assertEquals(1, result.totalPages());
        assertEquals(2, result.content().size());
        assertEquals(TEST_AUDIT_LOG_1, result.content().get(0));
        assertEquals(TEST_AUDIT_LOG_2, result.content().get(1));

        verify(auditLogRepository).findAll(query);
    }

    @Test
    public void givenEmptyPageQuery_whenGetAllAuditLogs_thenReturnEmptyPagedResponse() {
        PageQuery query = new PageQuery(0, 2);

        PagedResponse<AuditLog> mockedResponse = new PagedResponse<>(
                List.of(), // content empty
                0L,        // totalElements
                0,         // totalPages
                0,         // pageNumber
                true       // isLast
        );

        when(auditLogRepository.findAll(query)).thenReturn(mockedResponse);

        PagedResponse<AuditLog> result = getAllAuditLogsService.getAllAuditLogs(query);

        assertEquals(0, result.totalElements());
        assertEquals(0, result.totalPages());
        assertEquals(0, result.content().size());

        verify(auditLogRepository).findAll(query);
    }
}
