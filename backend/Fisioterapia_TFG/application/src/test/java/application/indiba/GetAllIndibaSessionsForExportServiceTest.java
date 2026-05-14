package application.indiba;

import com.tfg.indiba.IndibaSession;
import com.tfg.model.indiba.IndibaSessionFactory;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import com.tfg.patient.Patient;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.port.out.persistence.IndibaSessionRepository;
import com.tfg.service.indiba.GetAllIndibaSessionsForExportService;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GetAllIndibaSessionsForExportServiceTest {

    private final IndibaSessionRepository indibaSessionRepository = mock(IndibaSessionRepository.class);
    private final GetAllIndibaSessionsForExportService service =
            new GetAllIndibaSessionsForExportService(indibaSessionRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final Physiotherapist TEST_PHYSIO = PhysiotherapistFactory.createTestPsychiatrist("physio@gmail.com", "ValidPassword123!");
    private static final IndibaSession SESSION_1 = IndibaSessionFactory.createTestIndibaSession(
            TEST_PATIENT, TEST_PHYSIO, new Date(2024, 1, 10), new Date(2024, 1, 10));
    private static final IndibaSession SESSION_2 = IndibaSessionFactory.createTestIndibaSession(
            TEST_PATIENT, TEST_PHYSIO, new Date(2024, 2, 15), new Date(2024, 2, 15));

    @Test
    public void givenSessionsExist_whenGetAllForExport_returnList() {
        when(indibaSessionRepository.findAllForExport()).thenReturn(List.of(SESSION_1, SESSION_2));

        List<IndibaSession> result = service.getAllIndibaSessionsForExport();

        assertEquals(2, result.size());
        verify(indibaSessionRepository).findAllForExport();
    }

    @Test
    public void givenNoSessions_whenGetAllForExport_returnEmptyList() {
        when(indibaSessionRepository.findAllForExport()).thenReturn(List.of());

        List<IndibaSession> result = service.getAllIndibaSessionsForExport();

        assertEquals(0, result.size());
        verify(indibaSessionRepository).findAllForExport();
    }
}
