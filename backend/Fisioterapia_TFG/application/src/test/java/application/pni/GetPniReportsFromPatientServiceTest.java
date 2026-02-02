package application.pni;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.pni.PniReportFactory;
import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import com.tfg.pni.PniReport;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.PniReportRepository;
import com.tfg.service.pni.GetPniReportsFromPatientService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class GetPniReportsFromPatientServiceTest {

    private final PniReportRepository pniReportRepository = mock(PniReportRepository.class);
    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final GetPniReportsFromPatientService pniReportService = new GetPniReportsFromPatientService(pniReportRepository, patientRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final PniReport TEST_PNI_REPORT = PniReportFactory.createTestPniReport(
            TEST_PATIENT,
            5
    );
    private static final PniReport TEST_PNI_REPORT_2 = PniReportFactory.createTestPniReport(
            TEST_PATIENT,
            6
    );

    @Test
    public void givenPatientId_whenPniReportsExists_returnPniReports(){
        when(patientRepository.findById(new PatientId(1)))
                .thenReturn(Optional.of(TEST_PATIENT));

        when(pniReportRepository.findAllReportsByPatiendId(new PatientId(1)))
                .thenReturn(List.of(TEST_PNI_REPORT.getReportDate(), TEST_PNI_REPORT_2.getReportDate()));

        List<LocalDate> result = pniReportService.getPniReportsFromPatient(new PatientId(1));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(TEST_PNI_REPORT.getReportDate(), result.get(0));
        assertEquals(TEST_PNI_REPORT_2.getReportDate(), result.get(1));

        verify(pniReportRepository).findAllReportsByPatiendId(new PatientId(1));
    }

    @Test
    public void givenPatientId_whenPniReportsNotExists_returnEmptyList(){
        when(patientRepository.findById(new PatientId(1)))
                .thenReturn(Optional.of(TEST_PATIENT));

        when(pniReportRepository.findAllReportsByPatiendId(new PatientId(1)))
                .thenReturn(java.util.List.of());

        List<LocalDate> result = pniReportService.getPniReportsFromPatient(new PatientId(1));

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(pniReportRepository).findAllReportsByPatiendId(new PatientId(1));
    }

    @Test
    public void givenInvalidPatientId_whenGetPniReports_throwException(){
        when(patientRepository.findById(new PatientId(99)))
                .thenReturn(Optional.empty());

        try {
            pniReportService.getPniReportsFromPatient(new PatientId(99));
        } catch (InvalidIdException e) {
            assert(e.getMessage().equals("The provided ID is invalid."));
        }
    }
}
