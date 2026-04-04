package com.tfg.service.physiotherapist;

import com.tfg.port.in.physiotherapist.LogPhysiotherapistUseCase;
import com.tfg.pojos.springsecurity.AuthenticatedUser;
import com.tfg.port.out.springsecurity.CredentialsValidatorPort;
import com.tfg.port.out.springsecurity.TokenGeneratorPort;

public class LogPhysiotherapistService implements LogPhysiotherapistUseCase {
    private final TokenGeneratorPort tokenGeneratorPort;
    private final CredentialsValidatorPort credentialsValidatorPort;

    public LogPhysiotherapistService(TokenGeneratorPort tokenGeneratorPort, CredentialsValidatorPort credentialsValidatorPort) {
        this.tokenGeneratorPort = tokenGeneratorPort;
        this.credentialsValidatorPort = credentialsValidatorPort;
    }

    @Override
    public String authenticate(int physioId, String password) {
        AuthenticatedUser user = credentialsValidatorPort.validate(
                String.valueOf(physioId),
                password
        );
        return tokenGeneratorPort.generateToken(user);
    }
}
