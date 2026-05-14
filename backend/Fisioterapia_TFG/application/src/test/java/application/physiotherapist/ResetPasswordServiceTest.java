package application.physiotherapist;

import com.tfg.exceptions.InvalidTokenException;
import com.tfg.passwordresettoken.PasswordResetToken;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.port.out.persistence.PasswordResetTokenRepository;
import com.tfg.port.out.persistence.PhysiotherapistRepository;
import com.tfg.port.out.springsecurity.PasswordEncoderPort;
import com.tfg.service.physiotherapist.ResetPasswordService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ResetPasswordServiceTest {

    private final PasswordResetTokenRepository tokenRepository = mock(PasswordResetTokenRepository.class);
    private final PhysiotherapistRepository physiotherapistRepository = mock(PhysiotherapistRepository.class);
    private final PasswordEncoderPort passwordEncoderPort = mock(PasswordEncoderPort.class);

    private final ResetPasswordService resetPasswordService = new ResetPasswordService(
            tokenRepository,
            physiotherapistRepository,
            passwordEncoderPort
    );

    @Test
    public void givenValidToken_whenReset_thenUpdatePasswordAndDeleteToken() {
        String validTokenStr = "valid-uuid-token";
        String rawPassword = "newSecurePassword123";
        String encodedPassword = "encoded$Password$Hash";

        Physiotherapist mockPhysio = mock(Physiotherapist.class);

        PasswordResetToken validToken = mock(PasswordResetToken.class);
        when(validToken.isExpired()).thenReturn(false);
        when(validToken.physio()).thenReturn(mockPhysio);

        when(tokenRepository.findByToken(validTokenStr)).thenReturn(Optional.of(validToken));
        when(passwordEncoderPort.encode(rawPassword)).thenReturn(encodedPassword);

        resetPasswordService.reset(validTokenStr, rawPassword);

        verify(physiotherapistRepository).updatePassword(mockPhysio.getId(), encodedPassword);
        verify(tokenRepository).delete(validTokenStr);
    }

    @Test
    public void givenNonExistentToken_whenReset_thenThrowException() {
        String invalidTokenStr = "fake-token";
        when(tokenRepository.findByToken(invalidTokenStr)).thenReturn(Optional.empty());

        assertThrows(InvalidTokenException.class, () -> {
            resetPasswordService.reset(invalidTokenStr, "newPassword");
        }, "Should throw exception if token doesn't exist");

        verifyNoInteractions(passwordEncoderPort);
        verifyNoInteractions(physiotherapistRepository);
        verify(tokenRepository, never()).delete(anyString());
    }

    @Test
    public void givenExpiredToken_whenReset_thenDeleteTokenAndThrowException() {
        String expiredTokenStr = "expired-uuid-token";

        PasswordResetToken expiredToken = mock(PasswordResetToken.class);
        when(expiredToken.isExpired()).thenReturn(true);

        when(tokenRepository.findByToken(expiredTokenStr)).thenReturn(Optional.of(expiredToken));

        assertThrows(InvalidTokenException.class, () -> {
            resetPasswordService.reset(expiredTokenStr, "newPassword");
        }, "Should throw exception if token expires");

        verify(tokenRepository).delete(expiredTokenStr);
        verifyNoInteractions(passwordEncoderPort);
        verifyNoInteractions(physiotherapistRepository);
    }
}
