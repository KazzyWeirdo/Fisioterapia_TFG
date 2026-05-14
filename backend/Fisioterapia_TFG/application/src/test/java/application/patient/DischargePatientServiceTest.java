package application.patient;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.patient.Patient;
import com.tfg.model.patient.PatientId;
import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.application.service.patient.DischargePatientService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DischargePatientServiceTest {

    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final DischargePatientService service = new DischargePatientService(patientRepository);

    private static final Patient TEST_PATIENT =
            PatientFactory.createTestPatient("test@test.com", "12345678A");

    @Test
    public void givenExistingPatient_whenDischarge_thenDischargeDateSet() {
        when(patientRepository.findById(any(PatientId.class))).thenReturn(Optional.of(TEST_PATIENT));

        service.discharge(new PatientId(1));

        verify(patientRepository).save(argThat(p -> p.getDischargeDate() != null));
    }

    @Test
    public void givenAlreadyDischargedPatient_whenDischarge_thenDischargeDateUnchanged() {
        Patient discharged = PatientFactory.createTestPatient("discharged@test.com", "87654321B");
        discharged.discharge(java.time.LocalDate.of(2026, 1, 1));
        when(patientRepository.findById(any(PatientId.class))).thenReturn(Optional.of(discharged));

        service.discharge(new PatientId(1));

        verify(patientRepository).save(argThat(p ->
                p.getDischargeDate().equals(java.time.LocalDate.of(2026, 1, 1))
        ));
    }

    @Test
    public void givenUnknownPatient_whenDischarge_thenThrowsInvalidIdException() {
        when(patientRepository.findById(any(PatientId.class))).thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class, () -> service.discharge(new PatientId(999)));

        verify(patientRepository, never()).save(any());
    }
}
