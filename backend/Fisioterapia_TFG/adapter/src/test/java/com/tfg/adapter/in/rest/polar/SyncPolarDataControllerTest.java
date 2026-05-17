package com.tfg.adapter.in.rest.polar;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.application.port.in.polar.SyncPolarDataForPatientUseCase;
import com.tfg.model.patient.PatientId;
import com.tfg.application.exceptions.PatientNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SyncPolarDataControllerTest {

    @Mock
    private SyncPolarDataForPatientUseCase syncPolarDataUseCase;

    @InjectMocks
    private SyncPolarDataController syncPolarDataController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(syncPolarDataController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void syncForPatient_validPatient_returns204() throws Exception {
        doNothing().when(syncPolarDataUseCase).syncForPatient(any(PatientId.class));

        mockMvc.perform(post("/polar/sync/42"))
                .andExpect(status().isNoContent());

        verify(syncPolarDataUseCase, times(1)).syncForPatient(new PatientId(42));
    }

    @Test
    void syncForPatient_patientNotFound_returns404() throws Exception {
        doThrow(new PatientNotFoundException(new PatientId(999)))
                .when(syncPolarDataUseCase).syncForPatient(any(PatientId.class));

        mockMvc.perform(post("/polar/sync/999"))
                .andExpect(status().isNotFound());
    }
}
