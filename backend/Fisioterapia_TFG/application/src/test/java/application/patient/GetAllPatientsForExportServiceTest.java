package application.patient;

import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.service.patient.GetAllPatientsForExportService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GetAllPatientsForExportServiceTest {

    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final GetAllPatientsForExportService service =
            new GetAllPatientsForExportService(patientRepository);

    private static final Patient TEST_PATIENT_1 = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final Patient TEST_PATIENT_2 = PatientFactory.createTestPatient("hola2@gmail.com", "85728487G");

    @Test
    public void givenPatientsExist_whenGetAllForExport_returnList() {
        when(patientRepository.findAll()).thenReturn(List.of(TEST_PATIENT_1, TEST_PATIENT_2));

        List<Patient> result = service.getAllPatientsForExport();

        assertEquals(2, result.size());
        verify(patientRepository).findAll();
    }

    @Test
    public void givenNoPatients_whenGetAllForExport_returnEmptyList() {
        when(patientRepository.findAll()).thenReturn(List.of());

        List<Patient> result = service.getAllPatientsForExport();

        assertEquals(0, result.size());
        verify(patientRepository).findAll();
    }
}
