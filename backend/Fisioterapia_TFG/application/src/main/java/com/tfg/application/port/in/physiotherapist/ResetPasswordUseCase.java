package com.tfg.application.port.in.physiotherapist;

public interface ResetPasswordUseCase {
    void reset(String token, String newPassword);
}
