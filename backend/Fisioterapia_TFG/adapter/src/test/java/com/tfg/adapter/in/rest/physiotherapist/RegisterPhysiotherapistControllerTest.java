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
    void createPhysiotherapist_ShouldReturnOk_WhenInputIsValid() {
        RegisterPhysiotherapistModel model = new RegisterPhysiotherapistModel(
                "test@example.com",
                "John",
                "Doe",
                null,
                List.of("ADMIN")
        );

        given()
                .contentType("application/json")
                .body(model)
                .when()
                .post("/physiotherapist/register")
                .then()
                .status(HttpStatus.OK);
    }

    @Test
    void createPhysiotherapist_ShouldReturnBadRequest_WhenRolesIsEmpty() {
        RegisterPhysiotherapistModel model = new RegisterPhysiotherapistModel(
                "test@example.com",
                "John",
                "Doe",
                null,
                new ArrayList<>()
        );

        given()
                .contentType("application/json")
                .body(model)
                .when()
                .post("/physiotherapist/register")
                .then()
                .status(HttpStatus.BAD_REQUEST);
    }
}
