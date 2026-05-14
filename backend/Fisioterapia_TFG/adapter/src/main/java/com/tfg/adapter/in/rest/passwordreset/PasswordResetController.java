package com.tfg.adapter.in.rest.passwordreset;

import com.tfg.port.in.physiotherapist.RequestPasswordResetUseCase;
import com.tfg.port.in.physiotherapist.ResetPasswordUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password")
public class PasswordResetController {

    private final RequestPasswordResetUseCase requestPasswordResetUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;

    public PasswordResetController(RequestPasswordResetUseCase requestPasswordResetUseCase,
                                   ResetPasswordUseCase resetPasswordUseCase) {
        this.requestPasswordResetUseCase = requestPasswordResetUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
    }

    @PostMapping("/forgot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email to recover password sent succesfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        requestPasswordResetUseCase.requestReset(request.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        resetPasswordUseCase.reset(request.token(), request.newPassword());
        return ResponseEntity.ok().build();
    }
}