package application.indiba;

import com.tfg.indiba.IndibaSession;
import com.tfg.model.indiba.IndibaSessionFactory;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.port.out.persistence.IndibaSessionRepository;
import com.tfg.service.indiba.CreateIndibaSessionService;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CreateIndibaSessionServiceTest {

    private final IndibaSessionRepository indibaSessionRepository = mock(IndibaSessionRepository.class);
    private final CreateIndibaSessionService indibaSessionService = new CreateIndibaSessionService(indibaSessionRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final IndibaSession TEST_INDIBA_SESSION = new IndibaSessionFactory().createTestIndibaSession(TEST_PATIENT, new Date(2023, 11, 30), new Date(2023, 12, 15));

    @Test
    public void givenNewIndibaSession_createIndibaSession(){
        indibaSessionService.createIndibaSession(TEST_INDIBA_SESSION);

        verify(indibaSessionRepository).save(argThat(indibaSession ->
                indibaSession.getPatient().equals(TEST_PATIENT) &&
                indibaSession.getBeginSession().equals(new Date(2023, 11, 30)) &&
                indibaSession.getEndSession().equals(new Date(2023, 12, 15))
        ));
    }

}
