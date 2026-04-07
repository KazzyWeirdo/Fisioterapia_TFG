package com.tfg.adapter.in.rest.passwordreset;


import com.tfg.adapter.in.rest.common.GlobalExceptionHandler;
import com.tfg.exceptions.InvalidTokenException;
import com.tfg.port.in.physiotherapist.RequestPasswordResetUseCase;
import com.tfg.port.in.physiotherapist.ResetPasswordUseCase;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PasswordResetControllerTest {

    @Mock
    private RequestPasswordResetUseCase requestPasswordResetUseCase;

    @Mock
    private ResetPasswordUseCase resetPasswordUseCase;

    @InjectMocks
    private PasswordResetController passwordResetController;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(passwordResetController)
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }

    @Test
    public void givenValidEmail_whenForgotPassword_thenReturnsOk() {
        ForgotPasswordRequest request = new ForgotPasswordRequest("fisio@clinica.com");

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/password/forgot")
                .then()
                .statusCode(200);

        verify(requestPasswordResetUseCase).requestReset("fisio@clinica.com");
    }

    @Test
    public void givenValidTokenAndNewPassword_whenResetPassword_thenReturnsOk() {
        ResetPasswordRequest request = new ResetPasswordRequest("valid-uuid-token", "NewSecurePassword1!");

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/password/reset")
                .then()
                .statusCode(200);

        verify(resetPasswordUseCase).reset("valid-uuid-token", "NewSecurePassword1!");
    }

    @Test
    public void givenInvalidToken_whenResetPassword_thenReturnsErrorStatus() {
        ResetPasswordRequest request = new ResetPasswordRequest("invalid-token", "NewSecurePassword1!");

        doThrow(new InvalidTokenException("Invalid or non-existent token."))
                .when(resetPasswordUseCase).reset(request.token(), request.newPassword());

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/password/reset")
                .then()
                .statusCode(400);
    }
}
