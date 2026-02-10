package application.polar;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.polar.PolarRepository;
import com.tfg.service.polar.ManagePolarConnectionService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ManagePolarConnectionServiceTest {

    private final PolarRepository polarRepository = mock(PolarRepository.class);
    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final ManagePolarConnectionService managePolarConnectionService = new ManagePolarConnectionService(polarRepository, patientRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");

    @Test
    public void givenPatientId_initiateConnection() {
        managePolarConnectionService.initiateConnection(TEST_PATIENT.getId());

        verify(polarRepository).generateAuthUrl(String.valueOf(TEST_PATIENT.getId().value()));
    }

    @Test
    public void givenCodeAndPatientId_finalizeConnection() {
        String code = "test_code";
        when(patientRepository.findById(TEST_PATIENT.getId()))
                .thenReturn(Optional.of(TEST_PATIENT));

        when(polarRepository.exchangeCode(code))
                .thenReturn(new PolarRepository.PolarAuthResult("access_token", 1L));
        managePolarConnectionService.finalizeConnection(code, TEST_PATIENT.getId());

        verify(polarRepository).exchangeCode(code);
        verify(patientRepository).findById(TEST_PATIENT.getId());
    }

    @Test
    public void givenInvalidPatientId_finalizeConnection() {
        String code = "test_code";
        PatientId invalidPatientId = new PatientId(999);

        when(patientRepository.findById(invalidPatientId))
                .thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class, () -> {
            managePolarConnectionService.finalizeConnection(code, invalidPatientId);
        });
    }
}
