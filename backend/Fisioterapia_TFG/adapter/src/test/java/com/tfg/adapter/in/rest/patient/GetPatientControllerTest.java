package com.tfg.adapter.in.rest.patient;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import com.tfg.port.in.patient.GetPatientUseCase;
import com.tfg.port.out.persistence.PatientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GetPatientController.class)
public class GetPatientControllerTest {

    @MockBean GetPatientUseCase getPatientUseCase;
    @Autowired private MockMvc mockMvc;
    @MockBean PatientRepository patientRepository;

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");

    @Test
    public void givenValidId_whenGetPatient_thenReturnsPatient() throws Exception {
        given(getPatientUseCase.getPatient(new PatientId(TEST_PATIENT.getId().value())))
                .willReturn(TEST_PATIENT);

        mockMvc.perform(get("/patients/{patientId}", TEST_PATIENT.getId().value())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Esperamos HTTP 200
                .andExpect(jsonPath("$.id").value(TEST_PATIENT.getId().value()))
                .andExpect(jsonPath("$.name").value(TEST_PATIENT.getName()))
                .andExpect(jsonPath("$.email").value(TEST_PATIENT.getEmail().value()));
    }

    @Test
    public void givenInvalidId_whenGetPatient_thenReturnsNotFound() throws Exception {
        PatientId invalidId = new PatientId(9999);
        given(getPatientUseCase.getPatient(invalidId))
                .willThrow(new InvalidIdException());

        mockMvc.perform(get("/patients/{patientId}", invalidId.value())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
