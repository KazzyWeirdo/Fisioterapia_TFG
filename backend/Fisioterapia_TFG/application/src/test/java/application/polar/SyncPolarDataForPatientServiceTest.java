package application.polar;

import com.tfg.model.patient.PatientFactory;
import com.tfg.model.pni.PniReportFactory;
import com.tfg.model.patient.Patient;
import com.tfg.model.pni.PniReport;
import com.tfg.application.exceptions.PatientNotFoundException;
import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.application.port.out.persistence.PniReportRepository;
import com.tfg.application.port.out.polar.PolarRepository;
import com.tfg.application.service.polar.SyncPolarDataForPatientService;
import com.tfg.model.patient.PatientId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class SyncPolarDataForPatientServiceTest {
    private final PolarRepository polarRepository = mock(PolarRepository.class);
    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final PniReportRepository pniReportRepository = mock(PniReportRepository.class);
    private final SyncPolarDataForPatientService service =
            new SyncPolarDataForPatientService(patientRepository, pniReportRepository, polarRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "12345678D");
    private static final PniReport PNI_REPORT_DUMMY = PniReportFactory.createTestPniReport(TEST_PATIENT, 3);

    @Test
    public void givenNoExistingReport_syncForPatient_savesNewReport() {
        TEST_PATIENT.setPolarAccessToken("valid_token");
        TEST_PATIENT.setPolarUserId(1L);
        PatientId patientId = new PatientId(TEST_PATIENT.getId().value());

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(TEST_PATIENT));
        when(polarRepository.fetchDailyData(eq(TEST_PATIENT), any(LocalDate.class))).thenReturn(Optional.of(PNI_REPORT_DUMMY));
        when(pniReportRepository.findByPatientIdAndDate(eq(patientId), any(LocalDate.class))).thenReturn(Optional.empty());

        service.syncForPatient(patientId);

        verify(polarRepository, times(1)).fetchDailyData(eq(TEST_PATIENT), eq(LocalDate.now()));
        verify(pniReportRepository, times(1)).save(PNI_REPORT_DUMMY);
    }

    @Test
    public void givenExistingReportForToday_syncForPatient_updatesExistingReport() {
        TEST_PATIENT.setPolarAccessToken("valid_token");
        TEST_PATIENT.setPolarUserId(1L);
        PatientId patientId = new PatientId(TEST_PATIENT.getId().value());

        PniReport existingReport = PniReportFactory.createTestPniReport(TEST_PATIENT, 2);
        PniReport freshReport = PniReportFactory.createTestPniReport(TEST_PATIENT, 5);
        freshReport.setHours_asleep(9.0);
        freshReport.setAvg_hr(62.0);
        freshReport.setMin_hr(50);
        freshReport.setDeep_sleep(110);

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(TEST_PATIENT));
        when(polarRepository.fetchDailyData(eq(TEST_PATIENT), any(LocalDate.class))).thenReturn(Optional.of(freshReport));
        when(pniReportRepository.findByPatientIdAndDate(eq(patientId), any(LocalDate.class))).thenReturn(Optional.of(existingReport));

        service.syncForPatient(patientId);

        assertThat(existingReport.getHours_asleep()).isEqualTo(9.0);
        assertThat(existingReport.getAvg_hr()).isEqualTo(62.0);
        assertThat(existingReport.getMin_hr()).isEqualTo(50);
        assertThat(existingReport.getDeep_sleep()).isEqualTo(110);
        assertThat(existingReport.getContinuity()).isEqualTo(5);
        verify(pniReportRepository, times(1)).save(existingReport);
    }

    @Test
    public void givenPatientWithPolarToken_whenNoPolarData_syncForPatient_doesNotSave() {
        TEST_PATIENT.setPolarAccessToken("valid_token");
        TEST_PATIENT.setPolarUserId(1L);
        PatientId patientId = new PatientId(TEST_PATIENT.getId().value());

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(TEST_PATIENT));
        when(polarRepository.fetchDailyData(eq(TEST_PATIENT), any(LocalDate.class))).thenReturn(Optional.empty());

        service.syncForPatient(patientId);

        verify(pniReportRepository, never()).save(any());
    }

    @Test
    public void givenUnknownPatientId_syncForPatient_throwsPatientNotFoundException() {
        PatientId unknownId = new PatientId(9999);
        when(patientRepository.findById(unknownId)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class,
                () -> service.syncForPatient(unknownId));

        verify(polarRepository, never()).fetchDailyData(any(), any());
    }
}
