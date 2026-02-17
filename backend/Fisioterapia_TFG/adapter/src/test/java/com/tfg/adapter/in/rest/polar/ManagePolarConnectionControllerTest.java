package com.tfg.adapter.in.rest.polar;

import com.tfg.adapter.in.rest.common.PatientIdParser;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.port.in.polar.ManagePolarConnectionUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ManagePolarConnectionControllerTest {

    @Mock
    private ManagePolarConnectionUseCase managePolarConnectionUseCase;

    @InjectMocks
    private ManagePolarConnectionController managePolarConnectionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(managePolarConnectionController).build();
    }

    @Test
    void testAuthorize_ShouldRedirectToPolarAuthUrl() throws Exception {
        String patientId = "123";
        String expectedUrl = "https://auth.polar.com?state=" + patientId;

        when(managePolarConnectionUseCase.initiateConnection(PatientIdParser.parsePatientId(patientId)))
                .thenReturn(expectedUrl);

        mockMvc.perform(get("/api/auth/polar/authorize")
                        .param("patientId", patientId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(expectedUrl));
    }

    @Test
    void testCallback_ShouldReturnSuccessMessage() throws Exception {
        String code = "testCode";
        String state = "123";

        mockMvc.perform(get("/api/auth/polar/callback")
                        .param("code", code)
                        .param("state", state)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("¡Conexión con Polar Exitosa! Ya puedes cerrar esta ventana."));
    }
}
