package com.tfg.application.port.out.mail;

public interface EmailSenderPort {
    void sendPasswordResetLink(String email, String token);
}
