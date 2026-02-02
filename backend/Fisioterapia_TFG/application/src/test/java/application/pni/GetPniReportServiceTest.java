package application.pni;

import com.tfg.model.patient.PatientFactory;
import com.tfg.model.pni.PniReportFactory;
import com.tfg.patient.Patient;
import com.tfg.pni.PniReport;
import com.tfg.pni.PniReportId;
import com.tfg.port.out.persistence.PniReportRepository;
import com.tfg.service.pni.GetPniReportService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetPniReportServiceTest {

    private final PniReportRepository pniReportRepository = mock(PniReportRepository.class);
    private final GetPniReportService pniReportService = new GetPniReportService(pniReportRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final PniReport TEST_PNI_REPORT = PniReportFactory.createTestPniReport(
            TEST_PATIENT,
            5
    );
    private static final PniReportId INVALID_ID = new PniReportId(99);


    @Test
    public void givenPniReportId_whenPniReportNotExists_throwException(){
        when(pniReportRepository.findById(INVALID_ID))
                .thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            pniReportService.getPniReport(INVALID_ID);
        });
    }

    @Test
    public void givenPniReportId_whenPniReportExists_returnPniReport(){
        when(pniReportRepository.findById(TEST_PNI_REPORT.getId()))
                .thenReturn(Optional.of(TEST_PNI_REPORT));

        PniReport result = pniReportService.getPniReport(TEST_PNI_REPORT.getId());

        assertNotNull(result);
        assertEquals(result.getId(), TEST_PNI_REPORT.getId());

        verify(pniReportRepository).findById(TEST_PNI_REPORT.getId());
    }
}
