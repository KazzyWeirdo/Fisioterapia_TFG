package com.tfg.adapter.in.rest.patient;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.application.port.in.patient.DeletePatientUseCase;
import com.tfg.model.patient.PatientId;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeletePatientControllerTest {

    @Mock
    private DeletePatientUseCase deletePatientUseCase;

    @InjectMocks
    private DeletePatientController deletePatientController;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(deletePatientController)
                        .setControllerAdvice(new com.tfg.adapter.in.rest.common.GlobalExceptionHandler())
        );
    }

    @Test
    public void delete_ShouldReturnNoContent_WhenPatientExists() {
        given()
                .when()
                .delete("/patients/1")
                .then()
                .status(HttpStatus.NO_CONTENT);

        verify(deletePatientUseCase).delete(new PatientId(1));
    }

    @Test
    public void delete_ShouldReturnNotFound_WhenPatientDoesNotExist() {
        doThrow(new InvalidIdException())
                .when(deletePatientUseCase).delete(any(PatientId.class));

        given()
                .when()
                .delete("/patients/999")
                .then()
                .status(HttpStatus.NOT_FOUND);
    }
}
