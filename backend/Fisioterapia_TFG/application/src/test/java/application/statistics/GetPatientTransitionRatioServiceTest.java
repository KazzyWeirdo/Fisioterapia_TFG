package application.statistics;

import com.tfg.indiba.IndibaSession;
import com.tfg.model.indiba.IndibaSessionFactory;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import com.tfg.model.trainingsession.TrainingSessionFactory;
import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.port.out.persistence.IndibaSessionRepository;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.TrainingSessionRepository;
import com.tfg.service.statistics.GetPatientTransitionRatioService;
import com.tfg.statistics.PatientMonthTransitionRatio;
import com.tfg.trainingsession.TrainingSession;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class GetPatientTransitionRatioServiceTest {

    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final TrainingSessionRepository trainingSessionRepository = mock(TrainingSessionRepository.class);
    private final IndibaSessionRepository indibaSessionRepository = mock(IndibaSessionRepository.class);
    private final GetPatientTransitionRatioService getPatientTransitionRatioService = new GetPatientTransitionRatioService(patientRepository, indibaSessionRepository, trainingSessionRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final Physiotherapist TEST_PHYSIOTHERAPIST = PhysiotherapistFactory.createTestPsychiatrist("hola@gmail.com", "ValidPassword123!");
    private static final IndibaSession TEST_INDIBA_SESSION_1 = new IndibaSessionFactory().createTestIndibaSession(TEST_PATIENT, TEST_PHYSIOTHERAPIST, new Date(2024, 7, 15), new Date(2024, 7, 15));
    private static final TrainingSession TEST_TRAINING_SESSION = TrainingSessionFactory.createTestTrainingSession(TEST_PATIENT, LocalDate.of(2024, 7, 2));

    @Test
    public void givenPatientId_whenSessionsExists_giveStatistics() {
        List<Object[]> sessionCounts = List.of(
                new Object[]{1, 2},
                new Object[]{2, 2});

        when(patientRepository.findById(new PatientId(1)))
                .thenReturn(Optional.of(TEST_PATIENT));

        when(trainingSessionRepository.countSessionByMonth(new PatientId(1), TEST_TRAINING_SESSION.getDate().getYear()))
                .thenReturn(sessionCounts);

        when(indibaSessionRepository.countSessionGroupedByMonth(new PatientId(1), TEST_INDIBA_SESSION_1.getBeginSession().getYear()))
                .thenReturn(sessionCounts);

        List<PatientMonthTransitionRatio> result = getPatientTransitionRatioService.getTransitionRatio(new PatientId(1), TEST_TRAINING_SESSION.getDate().getYear());

        assertNotNull(result);
        assertEquals(12, result.size());
        assertEquals(2, result.get(0).trainingSessions());
        assertEquals(2, result.get(0).indibaSessions());

        verify(trainingSessionRepository).countSessionByMonth(new PatientId(1), TEST_TRAINING_SESSION.getDate().getYear());
        verify(indibaSessionRepository).countSessionGroupedByMonth(new PatientId(1), TEST_INDIBA_SESSION_1.getBeginSession().getYear());
    }

    @Test
    public void givenPatientId_whenNoSessionExists_giveEmptyList() {
        when(patientRepository.findById(new PatientId(1)))
                .thenReturn(Optional.of(TEST_PATIENT));

        when(trainingSessionRepository.countSessionByMonth(new PatientId(1), TEST_TRAINING_SESSION.getDate().getYear()))
                .thenReturn(List.of());

        when(indibaSessionRepository.countSessionGroupedByMonth(new PatientId(1), TEST_INDIBA_SESSION_1.getBeginSession().getYear()))
                .thenReturn(List.of());

        List<PatientMonthTransitionRatio> result = getPatientTransitionRatioService.getTransitionRatio(new PatientId(1), TEST_TRAINING_SESSION.getDate().getYear());

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(trainingSessionRepository).countSessionByMonth(new PatientId(1), TEST_TRAINING_SESSION.getDate().getYear());
        verify(indibaSessionRepository).countSessionGroupedByMonth(new PatientId(1), TEST_INDIBA_SESSION_1.getBeginSession().getYear());
    }

    @Test
    public void giveUnexistingPatientId_throwException() {
        when(patientRepository.findById(new PatientId(1)))
                .thenReturn(Optional.empty());

        try {
            getPatientTransitionRatioService.getTransitionRatio(new PatientId(1), TEST_TRAINING_SESSION.getDate().getYear());
        } catch (RuntimeException e) {
            assertEquals("The provided ID is invalid.", e.getMessage());
        }
    }
}
