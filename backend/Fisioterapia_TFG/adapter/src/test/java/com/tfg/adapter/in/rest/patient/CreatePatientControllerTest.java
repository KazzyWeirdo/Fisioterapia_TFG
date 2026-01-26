package com.tfg.adapter.in.rest.patient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.patient.PatientGender;
import com.tfg.port.in.patient.CreatePatientUseCase;
import com.tfg.port.out.persistence.PatientRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreatePatientController.class)
public class CreatePatientControllerTest {

    @MockBean CreatePatientUseCase createPatientUseCase;
    @MockBean PatientRepository patientRepository;
    @Autowired private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPatient_ShouldReturnOk_WhenInputIsValid() throws Exception {
        PatientCreationModel patientCreationModel = new PatientCreationModel(
                "test@example.com",
                "12345678A",
                "MALE",
                "John",
                "Doe",
                "Smith",
                123456789,
                LocalDate.of(1990, 1, 1)

        );

        mockMvc.perform(post("/patients/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientCreationModel)))
                .andExpect(status().isOk());

        Mockito.verify(createPatientUseCase).createPatient(
                "test@example.com",
                "12345678A",
                PatientGender.MALE,
                "John",
                "Doe",
                "Smith",
                LocalDate.of(1990, 1, 1),
                123456789
        );
    }

    @Test
    void createPatient_ShouldReturnBadRequest_WhenInputIsInvalid() throws Exception {
        PatientCreationModel invalidPatientCreationModel = new PatientCreationModel(
                "", // Invalid email
                "12345678A",
                "MALE",
                "John",
                "Doe",
                "Smith",
                123456789,
                LocalDate.of(1990, 1, 1)
        );

        mockMvc.perform(post("/patients/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPatientCreationModel)))
                .andExpect(status().isBadRequest());
    }
}
