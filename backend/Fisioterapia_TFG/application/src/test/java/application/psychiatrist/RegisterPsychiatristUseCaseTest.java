package application.psychiatrist;

import com.tfg.model.psychiatrist.PsychiatristFactory;
import com.tfg.port.out.persistence.PsychiatristRepository;
import com.tfg.psychiatrist.Psychiatrist;
import com.tfg.psychiatrist.PsychiatristEmail;
import com.tfg.service.psychiatrist.RegisterPsychiatristService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RegisterPsychiatristUseCaseTest {
    private final PsychiatristRepository psychiatristRepository = mock(PsychiatristRepository.class);
    private final RegisterPsychiatristService registerPsychiatristService = new RegisterPsychiatristService(psychiatristRepository);

    private static final Psychiatrist TEST_PSYCHIATRIST = PsychiatristFactory.createTestPsychiatrist("hola@gmail.com", "ValidPass123!");
    private static final Psychiatrist TEST_INCORRECT_PSYCHIATRIST = PsychiatristFactory.createTestPsychiatrist("hola@gmail.com", "123invalid");

    @Test
    public void givenNewPsychiatrist_whenPsychiatristNotExists_registerPsychiatrist(){
        when(psychiatristRepository.findByEmail(any(PsychiatristEmail.class)))
                .thenReturn(java.util.Optional.empty());

        registerPsychiatristService.registerPsychiatrist(TEST_PSYCHIATRIST);

        verify(psychiatristRepository).save(TEST_PSYCHIATRIST);
    }

    @Test
    public void givenNewPsychiatrist_withExistingEmail_throwException(){
        when(psychiatristRepository.findByEmail(any(PsychiatristEmail.class)))
                .thenReturn(Optional.of(TEST_PSYCHIATRIST));

        assertThrows(IllegalArgumentException.class, () -> {
            registerPsychiatristService.registerPsychiatrist(TEST_PSYCHIATRIST);
        });

        verify(psychiatristRepository, never()).save(any());
    }
}
