package com.tfg.adapter.in.rest.auditlog;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.auditlog.AuditLog;
import com.tfg.model.auditlog.AuditLogFactory;
import com.tfg.pojos.auditlog.PageQuery;
import com.tfg.pojos.auditlog.PagedResponse;
import com.tfg.port.in.auditlog.GetAllAuditLogsUseCase;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAllAuditLogsControllerTest {
    @Mock
    private GetAllAuditLogsUseCase getAllAuditLogsUseCase;

    @InjectMocks
    private GetAllAuditLogsController getAllAuditLogsController;

    private final AuditLog testAuditLog = AuditLogFactory.createTestAuditLog("Uno");

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getAllAuditLogsController)
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    void givenAuditLogs_whenGetLogs_thenReturnOk() {
        PagedResponse<AuditLog> pagedResponse = new PagedResponse<>(
                List.of(testAuditLog), 1, 1, 0, true
        );
        when(getAllAuditLogsUseCase.getAllAuditLogs(any(PageQuery.class))).thenReturn(pagedResponse);

        Pageable springPageable = PageRequest.of(0, 10);

        ResponseEntity<?> response = getAllAuditLogsController.getLogs(springPageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, ((PagedResponse<?>) response.getBody()).content().size());
    }

    @Test
    void givenNoAuditLogs_whenGetLogs_thenReturnNoContent() {
        PagedResponse<AuditLog> pagedResponse = new PagedResponse<>(List.of(), 0, 0, 0, true);
        when(getAllAuditLogsUseCase.getAllAuditLogs(any(PageQuery.class))).thenReturn(pagedResponse);

        Pageable springPageable = PageRequest.of(0, 10);

        ResponseEntity<?> response = getAllAuditLogsController.getLogs(springPageable);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
