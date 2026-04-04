package com.tfg.port.out.springsecurity;

import com.tfg.pojos.springsecurity.AuthenticatedUser;

public interface CredentialsValidatorPort {
    AuthenticatedUser validate(String subject, String password);

}
