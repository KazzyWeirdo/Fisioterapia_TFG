package application.trainingsession;

import com.tfg.model.patient.PatientFactory;
import com.tfg.model.trainingsession.ExerciseFactory;
import com.tfg.model.trainingsession.ExerciseSetFactory;
import com.tfg.model.trainingsession.TrainingSessionFactory;
import com.tfg.patient.Patient;
import com.tfg.port.out.persistence.TrainingSessionRepository;
import com.tfg.service.trainingsession.CreateTrainingSessionService;
import com.tfg.trainingsession.Exercise;
import com.tfg.trainingsession.ExerciseSet;
import com.tfg.trainingsession.TrainingSession;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CreateTrainingSessionServiceTest {

    private final TrainingSessionRepository trainingSessionRepository = mock(TrainingSessionRepository.class);
    private final CreateTrainingSessionService trainingSessionService = new CreateTrainingSessionService(trainingSessionRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final TrainingSession TEST_TRAINING_SESSION = TrainingSessionFactory.createTestTrainingSession(TEST_PATIENT, LocalDate.of(2024, 6, 1));

    @Test
    public void givenNewTrainingSession_createTrainingSession(){
        trainingSessionService.createTrainingSession(TEST_TRAINING_SESSION);

        verify(trainingSessionRepository).save(argThat(trainingSession ->
                 trainingSession.getPatient().equals(TEST_PATIENT) &&
                 trainingSession.getDate().equals(LocalDate.of(2024, 6, 1))
        ));
    }
}