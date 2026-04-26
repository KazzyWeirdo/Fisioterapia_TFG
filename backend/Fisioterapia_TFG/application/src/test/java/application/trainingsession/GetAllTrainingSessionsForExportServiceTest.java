package application.trainingsession;

import com.tfg.model.patient.PatientFactory;
import com.tfg.model.trainingsession.TrainingSessionFactory;
import com.tfg.patient.Patient;
import com.tfg.port.out.persistence.TrainingSessionRepository;
import com.tfg.service.trainingsession.GetAllTrainingSessionsForExportService;
import com.tfg.trainingsession.TrainingSession;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GetAllTrainingSessionsForExportServiceTest {

    private final TrainingSessionRepository trainingSessionRepository = mock(TrainingSessionRepository.class);
    private final GetAllTrainingSessionsForExportService service =
            new GetAllTrainingSessionsForExportService(trainingSessionRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final TrainingSession SESSION_1 = TrainingSessionFactory.createTestTrainingSession(TEST_PATIENT, LocalDate.of(2024, 1, 10));
    private static final TrainingSession SESSION_2 = TrainingSessionFactory.createTestTrainingSession(TEST_PATIENT, LocalDate.of(2024, 2, 15));

    @Test
    public void givenSessionsExist_whenGetAllForExport_returnList() {
        when(trainingSessionRepository.findAllForExport()).thenReturn(List.of(SESSION_1, SESSION_2));

        List<TrainingSession> result = service.getAllTrainingSessionsForExport();

        assertEquals(2, result.size());
        verify(trainingSessionRepository).findAllForExport();
    }

    @Test
    public void givenNoSessions_whenGetAllForExport_returnEmptyList() {
        when(trainingSessionRepository.findAllForExport()).thenReturn(List.of());

        List<TrainingSession> result = service.getAllTrainingSessionsForExport();

        assertEquals(0, result.size());
        verify(trainingSessionRepository).findAllForExport();
    }
}
