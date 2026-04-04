package com.tfg.adapter.in.rest.physiotherapist;

import com.tfg.port.in.physiotherapist.RegisterPhysiotherapistUseCase;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

@ExtendWith(MockitoExtension.class)
public class RegisterPhysiotherapistControllerTest {

    @Mock
    private RegisterPhysiotherapistUseCase registerPsychiatristUseCase;

    @InjectMocks
    private RegisterPhysiotherapistController registerPsychiatristController;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(registerPsychiatristController);
    }

    @Test
    void createPatient_ShouldReturnOk_WhenInputIsValid() throws Exception {
        List<String> roles = List.of("ADMIN");
        RegisterPhysiotherapistModel registerPsychiatristModel = new RegisterPhysiotherapistModel(
                "test@example.com",
                "ValidPass123!",
                "John",
                "Doe",
                roles

        );

        given()
                .contentType("application/json")
                .body(registerPsychiatristModel)
                .when()
                .post("/physiotherapist/register")
                .then()
                .status(HttpStatus.OK);
    }

    @Test
    void createPatient_ShouldReturnBadRequest_WhenInputIsInvalid() {
        List<String> roles = new ArrayList<>();
        RegisterPhysiotherapistModel registerPsychiatristModel = new RegisterPhysiotherapistModel(
                "test@example.com",
                "12345678A",
                "John",
                "Doe",
                roles

        );

        given()
                .contentType("application/json")
                .body(registerPsychiatristModel)
                .when()
                .post("/physiotherapist/register")
                .then()
                .status(HttpStatus.BAD_REQUEST);
    }
}
