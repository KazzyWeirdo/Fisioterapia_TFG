package application.polar;

import com.tfg.model.patient.PatientFactory;
import com.tfg.model.pni.PniReportFactory;
import com.tfg.model.patient.Patient;
import com.tfg.model.pni.PniReport;
import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.application.port.out.persistence.PniReportRepository;
import com.tfg.application.port.out.polar.PolarRepository;
import com.tfg.application.service.polar.SyncPolarDataService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class SyncPolarDataServiceTest {
    private final PolarRepository polarRepository = mock(PolarRepository.class);
    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final PniReportRepository pniReportRepository = mock(PniReportRepository.class);
    private final SyncPolarDataService syncPolarDataService = new SyncPolarDataService(patientRepository, pniReportRepository, polarRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "12345678D");
    private static final PniReport PNI_REPORT_DUMMY = PniReportFactory.createTestPniReport(TEST_PATIENT, 3);

    @Test
    public void givenNoExistingReport_executeDailySync_savesNewReport() {
        TEST_PATIENT.setPolarAccessToken("valid_token");
        TEST_PATIENT.setPolarUserId(1L);
        when(patientRepository.findAllWithPolarToken()).thenReturn(List.of(TEST_PATIENT));
        when(polarRepository.fetchDailyData(eq(TEST_PATIENT), any(LocalDate.class)))
                .thenReturn(Optional.of(PNI_REPORT_DUMMY));
        when(pniReportRepository.findByPatientIdAndDate(eq(TEST_PATIENT.getId()), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        syncPolarDataService.executeDailySync();

        verify(polarRepository, times(1)).fetchDailyData(eq(TEST_PATIENT), eq(LocalDate.now().minusDays(1)));
        verify(pniReportRepository, times(1)).save(PNI_REPORT_DUMMY);
    }

    @Test
    public void givenExistingReportForYesterday_executeDailySync_updatesExistingReport() {
        TEST_PATIENT.setPolarAccessToken("valid_token");
        TEST_PATIENT.setPolarUserId(1L);

        PniReport existingReport = PniReportFactory.createTestPniReport(TEST_PATIENT, 2);
        PniReport freshReport = PniReportFactory.createTestPniReport(TEST_PATIENT, 4);
        freshReport.setHours_asleep(8.5);
        freshReport.setAvg_hr(60.0);
        freshReport.setMin_hr(50);
        freshReport.setDeep_sleep(100);

        when(patientRepository.findAllWithPolarToken()).thenReturn(List.of(TEST_PATIENT));
        when(polarRepository.fetchDailyData(eq(TEST_PATIENT), any(LocalDate.class)))
                .thenReturn(Optional.of(freshReport));
        when(pniReportRepository.findByPatientIdAndDate(eq(TEST_PATIENT.getId()), any(LocalDate.class)))
                .thenReturn(Optional.of(existingReport));

        syncPolarDataService.executeDailySync();

        assertThat(existingReport.getHours_asleep()).isEqualTo(8.5);
        assertThat(existingReport.getAvg_hr()).isEqualTo(60.0);
        assertThat(existingReport.getMin_hr()).isEqualTo(50);
        assertThat(existingReport.getDeep_sleep()).isEqualTo(100);
        assertThat(existingReport.getContinuity()).isEqualTo(4);
        verify(pniReportRepository, times(1)).save(existingReport);
    }

    @Test
    public void whenAccessTokenInvalid_throwException() {
        TEST_PATIENT.setPolarAccessToken("invalid_token");
        TEST_PATIENT.setPolarUserId(1L);
        when(patientRepository.findAllWithPolarToken()).thenReturn(List.of(TEST_PATIENT));
        when(polarRepository.fetchDailyData(eq(TEST_PATIENT), any(LocalDate.class)))
                .thenThrow(new RuntimeException("Invalid token"));

        syncPolarDataService.executeDailySync();

        verify(polarRepository, times(1)).fetchDailyData(eq(TEST_PATIENT), eq(LocalDate.now().minusDays(1)));
        verify(pniReportRepository, times(0)).save(any());
    }
}
