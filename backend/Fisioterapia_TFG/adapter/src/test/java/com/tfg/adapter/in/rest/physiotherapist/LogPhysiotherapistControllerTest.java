package com.tfg.adapter.in.rest.physiotherapist;

import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.exceptions.BadCredentialsException;
import com.tfg.port.in.physiotherapist.LogPhysiotherapistUseCase;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogPhysiotherapistControllerTest {
    @Mock
    private LogPhysiotherapistUseCase logPhysiotherapistUseCase;

    @InjectMocks
    private LogPhysiotherapistController logPhysiotherapistController;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                logPhysiotherapistController,
                new GlobalExceptionHandler()
        );
        ReflectionTestUtils.setField(logPhysiotherapistController, "tokenPrefix", "Bearer ");
    }

    @Test
    void login_shouldReturn200_withTokenInBodyAndHeader() {
        AuthenticationRequest request = new AuthenticationRequest("physio@example.com", "password");
        when(logPhysiotherapistUseCase.authenticate("physio@example.com", "password")).thenReturn("jwt-token");

        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/physiotherapist/login")
                .then()
                .status(HttpStatus.OK)
                .header("Authorization", "Bearer jwt-token");
    }

    @Test
    void login_shouldReturn400_whenEmailIsBlank() {
        String body = """
                { "email": "", "password": "password" }
                """;

        given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/physiotherapist/login")
                .then()
                .status(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(logPhysiotherapistUseCase);
    }

    @Test
    void login_shouldReturn400_whenEmailIsInvalid() {
        String body = """
                { "email": "not-an-email", "password": "password" }
                """;

        given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/physiotherapist/login")
                .then()
                .status(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(logPhysiotherapistUseCase);
    }

    @Test
    void login_shouldReturn400_whenPasswordIsBlank() {
        String body = """
                { "email": "physio@example.com", "password": "" }
                """;

        given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/physiotherapist/login")
                .then()
                .status(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(logPhysiotherapistUseCase);
    }

    @Test
    void login_shouldReturn401_whenCredentialsAreWrong() {
        AuthenticationRequest request = new AuthenticationRequest("physio@example.com", "wrong");
        when(logPhysiotherapistUseCase.authenticate("physio@example.com", "wrong"))
                .thenThrow(new BadCredentialsException());

        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/physiotherapist/login")
                .then()
                .status(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void login_shouldCallUseCase_withCorrectArguments() {
        AuthenticationRequest request = new AuthenticationRequest("physio@example.com", "mypass");
        when(logPhysiotherapistUseCase.authenticate("physio@example.com", "mypass")).thenReturn("token");

        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/physiotherapist/login")
                .then()
                .status(HttpStatus.OK);

        verify(logPhysiotherapistUseCase).authenticate("physio@example.com", "mypass");
    }
}
