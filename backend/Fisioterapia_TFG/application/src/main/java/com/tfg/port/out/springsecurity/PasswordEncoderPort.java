package com.tfg.port.out.springsecurity;

public interface PasswordEncoderPort {
    String encode(String rawPassword);
}