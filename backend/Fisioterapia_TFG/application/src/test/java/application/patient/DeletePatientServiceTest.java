package application.patient;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.application.port.out.persistence.IndibaSessionRepository;
import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.application.port.out.persistence.PniReportRepository;
import com.tfg.application.port.out.persistence.TrainingSessionRepository;
import com.tfg.application.service.patient.DeletePatientService;
import com.tfg.model.patient.Patient;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.patient.PatientId;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeletePatientServiceTest {

    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final IndibaSessionRepository indibaSessionRepository = mock(IndibaSessionRepository.class);
    private final PniReportRepository pniReportRepository = mock(PniReportRepository.class);
    private final TrainingSessionRepository trainingSessionRepository = mock(TrainingSessionRepository.class);

    private final DeletePatientService service = new DeletePatientService(
            patientRepository, indibaSessionRepository, pniReportRepository, trainingSessionRepository);

    private static final Patient TEST_PATIENT =
            PatientFactory.createTestPatient("test@test.com", "12345678A");

    @Test
    public void givenExistingPatient_whenDelete_thenAllRelatedDataAndPatientDeleted() {
        PatientId id = new PatientId(1);
        when(patientRepository.findById(id)).thenReturn(Optional.of(TEST_PATIENT));

        service.delete(id);

        verify(indibaSessionRepository).deleteAllByPatientId(id);
        verify(pniReportRepository).deleteAllByPatientId(id);
        verify(trainingSessionRepository).deleteAllByPatientId(id);
        verify(patientRepository).deleteById(id);
    }

    @Test
    public void givenUnknownPatient_whenDelete_thenThrowsInvalidIdException() {
        PatientId id = new PatientId(999);
        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class, () -> service.delete(id));

        verify(patientRepository, never()).deleteById(any());
        verify(indibaSessionRepository, never()).deleteAllByPatientId(any());
        verify(pniReportRepository, never()).deleteAllByPatientId(any());
        verify(trainingSessionRepository, never()).deleteAllByPatientId(any());
    }

    @Test
    public void givenExistingPatient_whenDelete_thenSessionsDeletedBeforePatient() {
        PatientId id = new PatientId(1);
        when(patientRepository.findById(id)).thenReturn(Optional.of(TEST_PATIENT));

        var order = inOrder(indibaSessionRepository, pniReportRepository,
                trainingSessionRepository, patientRepository);

        service.delete(id);

        order.verify(indibaSessionRepository).deleteAllByPatientId(id);
        order.verify(pniReportRepository).deleteAllByPatientId(id);
        order.verify(trainingSessionRepository).deleteAllByPatientId(id);
        order.verify(patientRepository).deleteById(id);
    }
}
