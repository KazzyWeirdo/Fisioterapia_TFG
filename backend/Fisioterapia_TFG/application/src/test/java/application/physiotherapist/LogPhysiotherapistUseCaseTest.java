package application.physiotherapist;

import com.tfg.exceptions.BadCredentialsException;
import com.tfg.pojos.springsecurity.AuthenticatedUser;
import com.tfg.port.out.springsecurity.CredentialsValidatorPort;
import com.tfg.port.out.springsecurity.TokenGeneratorPort;
import com.tfg.service.physiotherapist.LogPhysiotherapistService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class LogPhysiotherapistUseCaseTest {
    private final TokenGeneratorPort tokenGeneratorPort = mock(TokenGeneratorPort.class);
    private final CredentialsValidatorPort credentialsValidatorPort = mock(CredentialsValidatorPort.class);
    private final LogPhysiotherapistService logPhysiotherapistService = new LogPhysiotherapistService(tokenGeneratorPort, credentialsValidatorPort);

    private static final AuthenticatedUser AUTHENTICATED_USER =
            new AuthenticatedUser("123", List.of("USER"));

    private static final String EXPECTED_TOKEN = "eyJhbGciOiJIUzUxMiJ9.test.token";

    @Test
    void authenticate_shouldReturnToken_whenCredentialsAreValid() {
        when(credentialsValidatorPort.validate("123", "password123"))
                .thenReturn(AUTHENTICATED_USER);
        when(tokenGeneratorPort.generateToken(AUTHENTICATED_USER))
                .thenReturn(EXPECTED_TOKEN);

        String result = logPhysiotherapistService.authenticate(123, "password123");

        assertThat(result).isEqualTo(EXPECTED_TOKEN);
    }

    @Test
    void authenticate_shouldCallValidatorWithCorrectSubject() {
        when(credentialsValidatorPort.validate(any(), any())).thenReturn(AUTHENTICATED_USER);
        when(tokenGeneratorPort.generateToken(any())).thenReturn(EXPECTED_TOKEN);

        logPhysiotherapistService.authenticate(123, "password123");

        verify(credentialsValidatorPort).validate("123", "password123");
    }

    @Test
    void authenticate_shouldPassAuthenticatedUserToTokenGenerator() {
        when(credentialsValidatorPort.validate(any(), any())).thenReturn(AUTHENTICATED_USER);
        when(tokenGeneratorPort.generateToken(any())).thenReturn(EXPECTED_TOKEN);

        logPhysiotherapistService.authenticate(123, "password123");

        verify(tokenGeneratorPort).generateToken(AUTHENTICATED_USER);
    }

    @Test
    void authenticate_shouldThrowException_whenValidatorFails() {
        when(credentialsValidatorPort.validate(any(), any()))
                .thenThrow(new BadCredentialsException());

        assertThatThrownBy(() -> logPhysiotherapistService.authenticate(123, "wrongpassword"))
                .isInstanceOf(BadCredentialsException.class);

        verifyNoInteractions(tokenGeneratorPort);
    }

    @Test
    void authenticate_shouldNeverCallTokenGenerator_whenValidationFails() {
        when(credentialsValidatorPort.validate(any(), any()))
                .thenThrow(new RuntimeException("Unexpected error"));

        assertThatThrownBy(() -> logPhysiotherapistService.authenticate(123, "password123"))
                .isInstanceOf(RuntimeException.class);

        verifyNoInteractions(tokenGeneratorPort);
    }
}
