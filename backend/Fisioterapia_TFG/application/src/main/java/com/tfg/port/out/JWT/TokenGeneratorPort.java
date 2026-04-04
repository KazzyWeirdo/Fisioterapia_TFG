package com.tfg.port.out.JWT;

public interface TokenGeneratorPort {
    String generateToken(AuthenticatedUser authentication);
}
