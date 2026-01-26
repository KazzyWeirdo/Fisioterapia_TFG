package application.patient;

import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.service.patient.GetPatientService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetPatientServiceTest {

    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final GetPatientService patientService = new GetPatientService(patientRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final PatientId EXISTING_ID = new PatientId(1);
    private static final PatientId NON_EXISTING_ID = new PatientId(99);

    @Test
    public void givenPatientId_whenPatientExists_returnPatient(){
        when(patientRepository.findById(EXISTING_ID))
                .thenReturn(Optional.of(TEST_PATIENT));

        Patient result = patientService.getPatient(EXISTING_ID);

        assertNotNull(result);
        assertEquals(TEST_PATIENT.getDni(), result.getDni());

        verify(patientRepository).findById(EXISTING_ID);
    }

    @Test
    public void givenPatientId_whenPatientNotExists_throwException(){
        when(patientRepository.findById(NON_EXISTING_ID))
                .thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> {
            patientService.getPatient(NON_EXISTING_ID);
        });
    }
}
