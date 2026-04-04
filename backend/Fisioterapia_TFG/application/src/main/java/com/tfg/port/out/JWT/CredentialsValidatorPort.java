package com.tfg.port.out.JWT;

public interface CredentialsValidatorPort {
    AuthenticatedUser validate(String subject, String password);

}
