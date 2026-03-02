package application.trainingsession;

import com.tfg.model.patient.PatientFactory;
import com.tfg.model.trainingsession.TrainingSessionFactory;
import com.tfg.patient.Patient;
import com.tfg.port.out.persistence.TrainingSessionRepository;
import com.tfg.service.trainingsession.GetTrainingSessionService;
import com.tfg.trainingsession.TrainingSession;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetTrainingSessionServiceTest {

    private final TrainingSessionRepository trainingSessionRepository = mock(TrainingSessionRepository.class);
    private final GetTrainingSessionService trainingSessionService = new GetTrainingSessionService(trainingSessionRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "12345678D");
    private static final TrainingSession TEST_TRAINING_SESSION = TrainingSessionFactory.createTestTrainingSession(TEST_PATIENT, LocalDate.of(2024, 6, 1));

    @Test
    public void givenExistingTrainingSession_getTrainingSession() {
        when(trainingSessionRepository.findById(TEST_TRAINING_SESSION.getId()))
                .thenReturn(java.util.Optional.of(TEST_TRAINING_SESSION));

        TrainingSession result = trainingSessionService.getTrainingSession(TEST_TRAINING_SESSION.getId());

        assertNotNull(result);
        assertEquals(TEST_TRAINING_SESSION.getId(), result.getId());

        verify(trainingSessionRepository).findById(TEST_TRAINING_SESSION.getId());
    }

    @Test
    public void givenUnexistingTrainigSession_throwException() {
        when(trainingSessionRepository.findById(TEST_TRAINING_SESSION.getId()))
                .thenReturn(java.util.Optional.empty());

        assertThrows(Exception.class, () -> {
            trainingSessionService.getTrainingSession(TEST_TRAINING_SESSION.getId());
        });

        verify(trainingSessionRepository).findById(TEST_TRAINING_SESSION.getId());
    }
}
