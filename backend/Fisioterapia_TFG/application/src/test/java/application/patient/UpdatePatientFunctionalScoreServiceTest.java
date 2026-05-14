package application.patient;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.patient.Patient;
import com.tfg.model.patient.PatientId;
import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.application.service.patient.UpdatePatientFunctionalScoreService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdatePatientFunctionalScoreServiceTest {

    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final UpdatePatientFunctionalScoreService service =
            new UpdatePatientFunctionalScoreService(patientRepository);

    private static final Patient TEST_PATIENT =
            PatientFactory.createTestPatient("test@test.com", "12345678A");

    @Test
    public void givenValidScore_whenUpdateFunctionalScore_thenScoreApplied() {
        when(patientRepository.findById(any(PatientId.class))).thenReturn(Optional.of(TEST_PATIENT));

        service.updateFunctionalScore(new PatientId(1), 75);

        verify(patientRepository).save(argThat(p -> p.getFunctionalScore() == 75));
    }

    @Test
    public void givenScore80_whenUpdateFunctionalScore_thenNoDischargeDateSetAutomatically() {
        when(patientRepository.findById(any(PatientId.class))).thenReturn(Optional.of(TEST_PATIENT));

        service.updateFunctionalScore(new PatientId(1), 80);

        verify(patientRepository).save(argThat(p ->
                p.getFunctionalScore() == 80 && p.getDischargeDate() == null
        ));
    }

    @Test
    public void givenUnknownPatient_whenUpdateFunctionalScore_thenThrowsInvalidIdException() {
        when(patientRepository.findById(any(PatientId.class))).thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class,
                () -> service.updateFunctionalScore(new PatientId(999), 50));

        verify(patientRepository, never()).save(any());
    }
}
