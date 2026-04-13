package com.tfg.port.out.mail;

public interface EmailSenderPort {
    void sendFormLink(String email, int id);
    void sendPasswordResetLink(String email, String token);
}
