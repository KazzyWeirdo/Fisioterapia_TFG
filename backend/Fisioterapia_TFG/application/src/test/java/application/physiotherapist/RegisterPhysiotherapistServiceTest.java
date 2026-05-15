package application.physiotherapist;

import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import com.tfg.application.port.in.physiotherapist.RequestPasswordResetUseCase;
import com.tfg.application.port.out.persistence.PhysiotherapistRepository;
import com.tfg.model.physiotherapist.Physiotherapist;
import com.tfg.model.physiotherapist.PhysiotherapistEmail;
import com.tfg.application.port.out.springsecurity.PasswordEncoderPort;
import com.tfg.application.service.physiotherapist.RegisterPhysiotherapistService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RegisterPhysiotherapistServiceTest {
    private final PhysiotherapistRepository psychiatristRepository = mock(PhysiotherapistRepository.class);
    private final PasswordEncoderPort passwordEncoderPort = mock(PasswordEncoderPort.class);
    private final RequestPasswordResetUseCase requestPasswordResetUseCase = mock(RequestPasswordResetUseCase.class);
    private final RegisterPhysiotherapistService registerPsychiatristService = new RegisterPhysiotherapistService(
            psychiatristRepository, passwordEncoderPort, requestPasswordResetUseCase);

    private static final Physiotherapist TEST_PSYCHIATRIST = PhysiotherapistFactory.createTestPsychiatrist("hola@gmail.com", "ValidPass123!");

    @Test
    public void givenNewPsychiatrist_whenPsychiatristNotExists_registerPsychiatrist(){
        when(psychiatristRepository.findByEmail(any(PhysiotherapistEmail.class)))
                .thenReturn(Optional.empty());
        when(passwordEncoderPort.encode(anyString())).thenReturn("encodedPassword");

        registerPsychiatristService.registerPsychiatrist(TEST_PSYCHIATRIST);

        verify(psychiatristRepository).save(TEST_PSYCHIATRIST);
        verify(requestPasswordResetUseCase).requestReset(TEST_PSYCHIATRIST.getEmail().value());
    }

    @Test
    public void givenNewPsychiatrist_withExistingEmail_throwException(){
        when(psychiatristRepository.findByEmail(any(PhysiotherapistEmail.class)))
                .thenReturn(Optional.of(TEST_PSYCHIATRIST));

        assertThrows(IllegalArgumentException.class, () -> {
            registerPsychiatristService.registerPsychiatrist(TEST_PSYCHIATRIST);
        });

        verify(psychiatristRepository, never()).save(any());
        verify(requestPasswordResetUseCase, never()).requestReset(anyString());
    }
}
