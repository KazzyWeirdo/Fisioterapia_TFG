package application.polar;

import com.tfg.model.patient.PatientFactory;
import com.tfg.model.pni.PniReportFactory;
import com.tfg.patient.Patient;
import com.tfg.pni.PniReport;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.PniReportRepository;
import com.tfg.port.out.polar.PolarRepository;
import com.tfg.service.polar.SyncPolarDataService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class SyncPolarDataServiceTest {
    private final PolarRepository polarRepository = mock(PolarRepository.class);
    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final PniReportRepository pniReportRepository = mock(PniReportRepository.class);
    private final SyncPolarDataService syncPolarDataService = new SyncPolarDataService(patientRepository, pniReportRepository, polarRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "12345678D");
    private static final PniReport PNI_REPORT_DUMMY = PniReportFactory.createTestPniReport(TEST_PATIENT,10);

    @Test
    public void givenListOfPatients_executeDailySync() {
        TEST_PATIENT.setPolarAccessToken("valid_token");
        TEST_PATIENT.setPolarUserId(1L);
        when(patientRepository.findAllWithPolarToken()).thenReturn(List.of(TEST_PATIENT));
        when(polarRepository.fetchDailyData(TEST_PATIENT))
                .thenReturn(Optional.of(PNI_REPORT_DUMMY));

        syncPolarDataService.executeDailySync();

        verify(polarRepository, times(1)).fetchDailyData(TEST_PATIENT);
        verify(pniReportRepository, times(1)).save(any());
    }

    @Test
    public void whenAccessTokenInvalid_throwException() {
        TEST_PATIENT.setPolarAccessToken("invalid_token");
        TEST_PATIENT.setPolarUserId(1L);
        when(patientRepository.findAllWithPolarToken()).thenReturn(List.of(TEST_PATIENT));
        when(polarRepository.fetchDailyData(TEST_PATIENT))
                .thenThrow(new RuntimeException("Invalid token"));

        syncPolarDataService.executeDailySync();

        verify(polarRepository, times(1)).fetchDailyData(TEST_PATIENT);
        verify(pniReportRepository, times(0)).save(any());
    }
}
