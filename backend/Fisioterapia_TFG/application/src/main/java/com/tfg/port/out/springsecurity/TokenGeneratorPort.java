package com.tfg.port.out.springsecurity;

import com.tfg.pojos.springsecurity.AuthenticatedUser;

public interface TokenGeneratorPort {
    String generateToken(AuthenticatedUser authentication);
}
