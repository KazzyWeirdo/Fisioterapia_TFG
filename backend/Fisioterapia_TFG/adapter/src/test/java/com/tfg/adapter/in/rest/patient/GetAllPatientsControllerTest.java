package com.tfg.adapter.in.rest.patient;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.auditlog.AuditLog;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.PatientSummaryElement;
import com.tfg.port.in.patient.GetAllPatientsUseCase;
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
public class GetAllPatientsControllerTest {
    @Mock
    private GetAllPatientsUseCase getAllPatientsUseCase;

    @InjectMocks
    private GetAllPatientsController getAllPatientsController;

    private final PatientSummaryElement TEST_PATIENT = new PatientSummaryElement(1, "Jane Doe");

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(getAllPatientsController)
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    void givenPatients_whenGetPatients_thenReturnOk() {
        PagedResponse<PatientSummaryElement> pagedResponse = new PagedResponse<>(
                List.of(TEST_PATIENT), 1, 1, 0, true
        );
        when(getAllPatientsUseCase.getAllPatients(any(PageQuery.class))).thenReturn(pagedResponse);

        Pageable springPageable = PageRequest.of(0, 10);

        ResponseEntity<?> response = getAllPatientsController.getAllPatients(springPageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, ((PagedResponse<?>) response.getBody()).content().size());
    }

    @Test
    void givenNoPatients_whenGetPatients_thenReturnNoContent() {
        PagedResponse<PatientSummaryElement> pagedResponse = new PagedResponse<>(List.of(), 0, 0, 0, true);
        when(getAllPatientsUseCase.getAllPatients(any(PageQuery.class))).thenReturn(pagedResponse);

        Pageable springPageable = PageRequest.of(0, 10);

        ResponseEntity<?> response = getAllPatientsController.getAllPatients(springPageable);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
