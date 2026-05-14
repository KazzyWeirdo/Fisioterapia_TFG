package application.pni;

import com.tfg.model.patient.PatientFactory;
import com.tfg.model.pni.PniReportFactory;
import com.tfg.patient.Patient;
import com.tfg.pni.PniReport;
import com.tfg.port.out.persistence.PniReportRepository;
import com.tfg.service.pni.GetAllPniReportsForExportService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GetAllPniReportsForExportServiceTest {

    private final PniReportRepository pniReportRepository = mock(PniReportRepository.class);
    private final GetAllPniReportsForExportService service =
            new GetAllPniReportsForExportService(pniReportRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final PniReport REPORT_1 = PniReportFactory.createTestPniReport(TEST_PATIENT, 75);
    private static final PniReport REPORT_2 = PniReportFactory.createTestPniReport(TEST_PATIENT, 80);

    @Test
    public void givenReportsExist_whenGetAllForExport_returnList() {
        when(pniReportRepository.findAllForExport()).thenReturn(List.of(REPORT_1, REPORT_2));

        List<PniReport> result = service.getAllPniReportsForExport();

        assertEquals(2, result.size());
        verify(pniReportRepository).findAllForExport();
    }

    @Test
    public void givenNoReports_whenGetAllForExport_returnEmptyList() {
        when(pniReportRepository.findAllForExport()).thenReturn(List.of());

        List<PniReport> result = service.getAllPniReportsForExport();

        assertEquals(0, result.size());
        verify(pniReportRepository).findAllForExport();
    }
}
