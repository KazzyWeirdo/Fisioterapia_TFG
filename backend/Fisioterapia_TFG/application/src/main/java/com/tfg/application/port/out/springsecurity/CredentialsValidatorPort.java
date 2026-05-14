package com.tfg.application.port.out.springsecurity;

import com.tfg.application.pojos.springsecurity.AuthenticatedUser;

public interface CredentialsValidatorPort {
    AuthenticatedUser validate(String subject, String password);

}
