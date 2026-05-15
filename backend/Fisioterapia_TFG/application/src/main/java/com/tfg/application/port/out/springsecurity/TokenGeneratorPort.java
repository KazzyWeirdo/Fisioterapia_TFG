package com.tfg.application.port.out.springsecurity;

import com.tfg.application.pojos.springsecurity.AuthenticatedUser;

public interface TokenGeneratorPort {
    String generateToken(AuthenticatedUser authentication);
}
