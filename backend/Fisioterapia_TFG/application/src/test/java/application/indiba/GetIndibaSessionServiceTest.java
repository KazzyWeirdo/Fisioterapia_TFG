package application.indiba;

import com.tfg.indiba.IndibaSession;
import com.tfg.indiba.IndibaSessionId;
import com.tfg.model.indiba.IndibaSessionFactory;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.port.out.persistence.IndibaSessionRepository;
import com.tfg.service.indiba.GetIndibaSessionService;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetIndibaSessionServiceTest {

    private final IndibaSessionRepository indibaSessionRepository = mock(IndibaSessionRepository.class);
    private final GetIndibaSessionService indibaSessionService = new GetIndibaSessionService(indibaSessionRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final IndibaSession TEST_INDIBA_SESSION = new IndibaSessionFactory().createTestIndibaSession(TEST_PATIENT, new Date(2023, 11, 30), new Date(2023, 12, 15));
    private static final IndibaSessionId EXISTING_ID = TEST_INDIBA_SESSION.getId();
    private static final IndibaSessionId NON_EXISTING_ID = new IndibaSessionId(99);

    @Test
    public void givenIndibaSessionId_whenIndibaSessionExists_returnIndibaSession(){
        when(indibaSessionRepository.findById(EXISTING_ID))
                .thenReturn(Optional.of(TEST_INDIBA_SESSION));

        IndibaSession result = indibaSessionService.getIndibaSession(EXISTING_ID);

        assertNotNull(result);
        assertEquals(result.getId(), result.getId());

        verify(indibaSessionRepository).findById(EXISTING_ID);
    }

    @Test
    public void givenIndibaSessionId_whenIndibaSessionNotExists_throwException(){
        when(indibaSessionRepository.findById(NON_EXISTING_ID))
                .thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            indibaSessionService.getIndibaSession(NON_EXISTING_ID);
        });
    }
}
