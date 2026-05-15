package com.tfg.application.port.out.springsecurity;

public interface PasswordEncoderPort {
    String encode(String rawPassword);
}