package com.tfg.port.out.mail;

public interface EmailSenderPort {
    void sendPasswordResetLink(String email, String token);
}
