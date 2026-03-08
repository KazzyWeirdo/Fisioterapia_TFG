package application.trainingsession;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.trainingsession.TrainingSessionFactory;
import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.TrainingSessionRepository;
import com.tfg.service.trainingsession.GetTrainingSessionByPatientService;
import com.tfg.trainingsession.TrainingSession;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class GetTrainingSessionByPatientServiceTest {

    private final TrainingSessionRepository trainingSessionRepository = mock(TrainingSessionRepository.class);
    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final GetTrainingSessionByPatientService getTrainingSessionByPatientService = new GetTrainingSessionByPatientService(trainingSessionRepository, patientRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "12345678D");
    private static final TrainingSession TEST_TRAINING_SESSION = TrainingSessionFactory.createTestTrainingSession(TEST_PATIENT, LocalDate.of(2024, 6, 1));
    private static final TrainingSession TEST_TRAINING_SESSION_2 = TrainingSessionFactory.createTestTrainingSession(TEST_PATIENT, LocalDate.of(2024, 7, 1));

    @Test
    public void givenPatiendId_whenTrainingSessionsExists_giveDates() {
        when(patientRepository.findById(new PatientId(1)))
                .thenReturn(Optional.of(TEST_PATIENT));

        when(trainingSessionRepository.findAllByPatientId(new PatientId(1)))
                .thenReturn(java.util.List.of(TEST_TRAINING_SESSION.getDate(), TEST_TRAINING_SESSION_2.getDate()));

        List<LocalDate> result = getTrainingSessionByPatientService.getTrainingSessionFromPatient(new PatientId(1));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(TEST_TRAINING_SESSION.getDate(), result.get(0));
        assertEquals(TEST_TRAINING_SESSION_2.getDate(), result.get(1));

        verify(trainingSessionRepository).findAllByPatientId(new PatientId(1));
    }

    @Test
    public void givenPatientId_whenNoTrainingSessionsExists_giveEmtpyList() {
        when(patientRepository.findById(new PatientId(1)))
                .thenReturn(Optional.of(TEST_PATIENT));

        when(trainingSessionRepository.findAllByPatientId(new PatientId(1)))
                .thenReturn(List.of());

        List<LocalDate> result = getTrainingSessionByPatientService.getTrainingSessionFromPatient(new PatientId(1));

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(trainingSessionRepository).findAllByPatientId(new PatientId(1));
    }

    @Test
    public void givenUnexistingPatientId_throwException() {
        when(patientRepository.findById(new PatientId(1)))
                .thenReturn(Optional.empty());

        try {
            getTrainingSessionByPatientService.getTrainingSessionFromPatient(new PatientId(1));
        } catch (InvalidIdException e) {
            assertEquals("The provided ID is invalid.", e.getMessage());
        }
    }
}
