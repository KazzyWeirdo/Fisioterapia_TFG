package application.physiotherapist;

import com.tfg.passwordresettoken.PasswordResetToken;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.physiotherapist.PhysiotherapistEmail;
import com.tfg.port.out.mail.EmailSenderPort;
import com.tfg.port.out.persistence.PasswordResetTokenRepository;
import com.tfg.port.out.persistence.PhysiotherapistRepository;
import com.tfg.service.physiotherapist.RequestPasswordResetService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RequestPasswordResetServiceTest {

    private final PhysiotherapistRepository physiotherapistRepository = mock(PhysiotherapistRepository.class);
    private final PasswordResetTokenRepository tokenRepository = mock(PasswordResetTokenRepository.class);
    private final EmailSenderPort emailSenderPort = mock(EmailSenderPort.class);

    private final RequestPasswordResetService resetService = new RequestPasswordResetService(
            physiotherapistRepository,
            tokenRepository,
            emailSenderPort
    );

    @Test
    public void givenExistingEmail_whenRequestReset_thenSaveTokenAndSendEmail() {
        String targetEmail = "fisio@clinica.com";
        Physiotherapist mockPhysio = mock(Physiotherapist.class);

        when(physiotherapistRepository.findByEmail(any(PhysiotherapistEmail.class)))
                .thenReturn(Optional.of(mockPhysio));

        resetService.requestReset(targetEmail);

        ArgumentCaptor<PasswordResetToken> tokenCaptor = ArgumentCaptor.forClass(PasswordResetToken.class);
        verify(tokenRepository).save(tokenCaptor.capture());

        PasswordResetToken savedToken = tokenCaptor.getValue();

        assertNotNull(savedToken);
        assertFalse(savedToken.token().isEmpty(), "Token UUID must be empty.");

        assertTrue(savedToken.expiresAt().isAfter(LocalDateTime.now()), "Expire date must be in the future.");

        verify(emailSenderPort).sendPasswordResetLink(eq(targetEmail), eq(savedToken.token()));
    }

    @Test
    public void givenNonExistingEmail_whenRequestReset_thenDoNothing() {
        String unknownEmail = "unexistent@clinica.com";

        when(physiotherapistRepository.findByEmail(any(PhysiotherapistEmail.class)))
                .thenReturn(Optional.empty());

        resetService.requestReset(unknownEmail);

        verify(tokenRepository, never()).save(any());

        verify(emailSenderPort, never()).sendPasswordResetLink(anyString(), anyString());
    }
}
