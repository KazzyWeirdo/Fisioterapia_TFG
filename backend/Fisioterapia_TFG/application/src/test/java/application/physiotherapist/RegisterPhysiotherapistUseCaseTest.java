package application.physiotherapist;

import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import com.tfg.port.out.persistence.PhysiotherapistRepository;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.physiotherapist.PhysiotherapistEmail;
import com.tfg.service.physiotherapist.RegisterPhysiotherapistService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RegisterPhysiotherapistUseCaseTest {
    private final PhysiotherapistRepository psychiatristRepository = mock(PhysiotherapistRepository.class);
    private final RegisterPhysiotherapistService registerPsychiatristService = new RegisterPhysiotherapistService(psychiatristRepository);

    private static final Physiotherapist TEST_PSYCHIATRIST = PhysiotherapistFactory.createTestPsychiatrist("hola@gmail.com", "ValidPass123!");

    @Test
    public void givenNewPsychiatrist_whenPsychiatristNotExists_registerPsychiatrist(){
        when(psychiatristRepository.findByEmail(any(PhysiotherapistEmail.class)))
                .thenReturn(java.util.Optional.empty());

        registerPsychiatristService.registerPsychiatrist(TEST_PSYCHIATRIST);

        verify(psychiatristRepository).save(TEST_PSYCHIATRIST);
    }

    @Test
    public void givenNewPsychiatrist_withExistingEmail_throwException(){
        when(psychiatristRepository.findByEmail(any(PhysiotherapistEmail.class)))
                .thenReturn(Optional.of(TEST_PSYCHIATRIST));

        assertThrows(IllegalArgumentException.class, () -> {
            registerPsychiatristService.registerPsychiatrist(TEST_PSYCHIATRIST);
        });

        verify(psychiatristRepository, never()).save(any());
    }
}
