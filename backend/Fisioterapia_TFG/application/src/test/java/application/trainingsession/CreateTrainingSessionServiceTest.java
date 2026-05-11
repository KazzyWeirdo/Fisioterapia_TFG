package application.trainingsession;

import com.tfg.model.patient.PatientFactory;
import com.tfg.model.trainingsession.TrainingSessionFactory;
import com.tfg.model.patient.Patient;
import com.tfg.application.port.out.persistence.TrainingSessionRepository;
import com.tfg.application.service.trainingsession.CreateTrainingSessionService;
import com.tfg.model.trainingsession.TrainingSession;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CreateTrainingSessionServiceTest {

    private final TrainingSessionRepository trainingSessionRepository = mock(TrainingSessionRepository.class);
    private final CreateTrainingSessionService trainingSessionService = new CreateTrainingSessionService(trainingSessionRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final TrainingSession TEST_TRAINING_SESSION = TrainingSessionFactory.createTestTrainingSession(TEST_PATIENT, LocalDateTime.of(2024, 6, 1, 10, 0), LocalDateTime.of(2024, 6, 1, 11, 0));

    @Test
    public void givenNewTrainingSession_createTrainingSession(){
        trainingSessionService.createTrainingSession(TEST_TRAINING_SESSION);

        verify(trainingSessionRepository).save(argThat(trainingSession ->
                 trainingSession.getPatient().equals(TEST_PATIENT) &&
                 trainingSession.getStartDateTime().equals(LocalDateTime.of(2024, 6, 1, 10, 0)) &&
                 trainingSession.getEndDateTime().equals(LocalDateTime.of(2024, 6, 1, 11, 0))
        ));
    }
}