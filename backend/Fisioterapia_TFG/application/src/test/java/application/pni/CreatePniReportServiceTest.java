package application.pni;

import com.tfg.model.patient.PatientFactory;
import com.tfg.model.pni.PniReportFactory;
import com.tfg.patient.Patient;
import com.tfg.pni.PniReport;
import com.tfg.port.out.persistence.PniReportRepository;
import com.tfg.service.pni.CreatePniReportService;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CreatePniReportServiceTest {

    private final PniReportRepository pniReportRepository = mock(PniReportRepository.class);
    private final CreatePniReportService pniReportService = new CreatePniReportService(pniReportRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final PniReport TEST_PNI_REPORT = PniReportFactory.createTestPniReport(
            TEST_PATIENT,
            5
    );

    @Test
    public void givenNewPniReport_createPniReport(){
        pniReportService.createPniReport(TEST_PNI_REPORT);

        verify(pniReportRepository).save(argThat(pniReport ->
                pniReport.getPatient().equals(TEST_PATIENT) &&
                pniReport.getNtrs() == 5
        ));
    }
}
