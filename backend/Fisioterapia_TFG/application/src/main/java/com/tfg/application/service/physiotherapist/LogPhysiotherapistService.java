package com.tfg.application.service.physiotherapist;

import com.tfg.application.port.in.physiotherapist.LogPhysiotherapistUseCase;
import com.tfg.application.pojos.springsecurity.AuthenticatedUser;
import com.tfg.application.port.out.springsecurity.CredentialsValidatorPort;
import com.tfg.application.port.out.springsecurity.TokenGeneratorPort;

public class LogPhysiotherapistService implements LogPhysiotherapistUseCase {
    private final TokenGeneratorPort tokenGeneratorPort;
    private final CredentialsValidatorPort credentialsValidatorPort;

    public LogPhysiotherapistService(TokenGeneratorPort tokenGeneratorPort, CredentialsValidatorPort credentialsValidatorPort) {
        this.tokenGeneratorPort = tokenGeneratorPort;
        this.credentialsValidatorPort = credentialsValidatorPort;
    }

    @Override
    public String authenticate(String email, String password) {
        AuthenticatedUser user = credentialsValidatorPort.validate(email, password);
        return tokenGeneratorPort.generateToken(user);
    }
}
