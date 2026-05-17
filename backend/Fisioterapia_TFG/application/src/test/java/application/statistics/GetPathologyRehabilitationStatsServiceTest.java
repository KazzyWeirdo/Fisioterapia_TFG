package application.statistics;

import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.application.service.statistics.GetPathologyRehabilitationStatsService;
import com.tfg.model.patient.Patient;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.statistics.PathologyRehabilitationStats;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetPathologyRehabilitationStatsServiceTest {

    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final GetPathologyRehabilitationStatsService service =
            new GetPathologyRehabilitationStatsService(patientRepository);

    @Test
    public void givenNoDischargedPatients_whenGetRehabStats_thenReturnEmptyList() {
        when(patientRepository.findAllDischarged()).thenReturn(List.of());

        List<PathologyRehabilitationStats> result = service.getPathologyRehabilitationStats();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void givenOneDischargedPatient_whenGetRehabStats_thenReturnOneEntry() {
        Patient patient = PatientFactory.createTestPatient("p1@test.com", "12345678A");
        patient.setRegistrationDate(LocalDate.of(2024, 1, 1));
        patient.setDischargeDate(LocalDate.of(2024, 1, 31));

        when(patientRepository.findAllDischarged()).thenReturn(List.of(patient));

        List<PathologyRehabilitationStats> result = service.getPathologyRehabilitationStats();

        assertEquals(1, result.size());
        assertEquals("TENDINOPATHY", result.get(0).pathology());
        assertEquals(30.0, result.get(0).averageDaysToDischarge(), 0.001);
        assertEquals(1, result.get(0).sampleSize());
    }

    @Test
    public void givenTwoPatientsWithSamePathology_whenGetRehabStats_thenAverageDaysIsCorrect() {
        Patient p1 = PatientFactory.createTestPatient("p1@test.com", "12345678A");
        p1.setRegistrationDate(LocalDate.of(2024, 1, 1));
        p1.setDischargeDate(LocalDate.of(2024, 1, 11));

        Patient p2 = PatientFactory.createTestPatient("p2@test.com", "87654321B");
        p2.setRegistrationDate(LocalDate.of(2024, 2, 1));
        p2.setDischargeDate(LocalDate.of(2024, 2, 21));

        when(patientRepository.findAllDischarged()).thenReturn(List.of(p1, p2));

        List<PathologyRehabilitationStats> result = service.getPathologyRehabilitationStats();

        assertEquals(1, result.size());
        assertEquals(15.0, result.get(0).averageDaysToDischarge(), 0.001);
        assertEquals(2, result.get(0).sampleSize());
    }

    @Test
    public void givenPatientsWithDifferentPathologies_whenGetRehabStats_thenReturnSortedByPathology() {
        Patient p1 = PatientFactory.createTestPatient("p1@test.com", "12345678A");
        p1.setRegistrationDate(LocalDate.of(2024, 1, 1));
        p1.setDischargeDate(LocalDate.of(2024, 1, 11));

        Patient p2 = PatientFactory.createTestPatient("p2@test.com", "87654321B");
        p2.setPathology(com.tfg.model.patient.Pathology.ANKLE_SPRAIN);
        p2.setRegistrationDate(LocalDate.of(2024, 2, 1));
        p2.setDischargeDate(LocalDate.of(2024, 2, 6));

        when(patientRepository.findAllDischarged()).thenReturn(List.of(p1, p2));

        List<PathologyRehabilitationStats> result = service.getPathologyRehabilitationStats();

        assertEquals(2, result.size());
        assertEquals("ANKLE_SPRAIN", result.get(0).pathology());
        assertEquals("TENDINOPATHY", result.get(1).pathology());
    }

    @Test
    public void givenPatientWithNullPathology_whenGetRehabStats_thenPatientIsIgnored() {
        Patient p1 = PatientFactory.createTestPatient("p1@test.com", "12345678A");
        p1.setRegistrationDate(LocalDate.of(2024, 1, 1));
        p1.setDischargeDate(LocalDate.of(2024, 1, 11));

        Patient p2 = PatientFactory.createTestPatient("p2@test.com", "87654321B");
        p2.setPathology(null);

        when(patientRepository.findAllDischarged()).thenReturn(List.of(p1, p2));

        List<PathologyRehabilitationStats> result = service.getPathologyRehabilitationStats();

        assertEquals(1, result.size());
    }
}
