package application.indiba;

import com.tfg.indiba.IndibaSession;
import com.tfg.model.indiba.IndibaSessionFactory;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import com.tfg.port.out.persistence.IndibaSessionRepository;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.service.indiba.GetIndibaSessionFromPatientService;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class GetIndibaSessionFromPatientServiceTest {

    private final IndibaSessionRepository indibaSessionRepository = mock(IndibaSessionRepository.class);
    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final GetIndibaSessionFromPatientService indibaSessionService = new GetIndibaSessionFromPatientService(indibaSessionRepository, patientRepository);

    private static final IndibaSession TEST_INDIBA_SESSION_1 = new IndibaSessionFactory().createTestIndibaSession(1, new Date(2023, 11, 30), new Date(2023, 12, 15));
    private static final IndibaSession TEST_INDIBA_SESSION_2 = new IndibaSessionFactory().createTestIndibaSession(1, new Date(2023, 11, 10), new Date(2023, 11, 20));
    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");

    @Test
    public void givenPatientId_whenIndibaSessionsExists_returnIndibaSessions(){
        when(patientRepository.findById(new PatientId(1)))
                .thenReturn(Optional.of(TEST_PATIENT));

        when(indibaSessionRepository.findAllByPatientId(new PatientId(1)))
                .thenReturn(List.of(TEST_INDIBA_SESSION_1, TEST_INDIBA_SESSION_2));

        List<IndibaSession> result = indibaSessionService.getIndibaSessionsFromPatient(new PatientId(1));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(TEST_INDIBA_SESSION_1.getBeginSession(), result.get(0).getBeginSession());
        assertEquals(TEST_INDIBA_SESSION_2.getBeginSession(), result.get(1).getBeginSession());

        verify(indibaSessionRepository).findAllByPatientId(new PatientId(1));
    }

    @Test
    public void givenPatientId_whenIndibaSessionsNotExists_returnEmptyList(){
        when(patientRepository.findById(new PatientId(1)))
                .thenReturn(Optional.of(TEST_PATIENT));

        when(indibaSessionRepository.findAllByPatientId(new PatientId(1))).thenReturn(List.of());

        List<IndibaSession> result = indibaSessionService.getIndibaSessionsFromPatient(new PatientId(1));

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(indibaSessionRepository).findAllByPatientId(new PatientId(1));
    }

    @Test
    public void givenUnexistingPatientId_whenGetIndibaSessions_throwException(){
        when(patientRepository.findById(new PatientId(99)))
                .thenReturn(Optional.empty());

        try {
            indibaSessionService.getIndibaSessionsFromPatient(new PatientId(99));
        } catch (Exception e) {
            assertEquals("The provided ID is invalid.", e.getMessage());
        }
    }
}
