package application.statistics;

import com.tfg.model.indiba.IndibaSession;
import com.tfg.model.indiba.IndibaSessionModes;
import com.tfg.model.patient.Patient;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.patient.PatientId;
import com.tfg.model.physiotherapist.Physiotherapist;
import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import com.tfg.application.port.out.persistence.IndibaSessionRepository;
import com.tfg.application.service.statistics.GetIndibaSessionStatsService;
import com.tfg.model.statistics.IndibaSessionStats;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GetIndibaSessionStatsServiceTest {

    private final IndibaSessionRepository indibaSessionRepository = mock(IndibaSessionRepository.class);
    private final GetIndibaSessionStatsService service =
            new GetIndibaSessionStatsService(indibaSessionRepository);

    private static final Patient PATIENT = PatientFactory.createTestPatient("test@test.com", "85729487J");
    private static final Physiotherapist PHYSIO = PhysiotherapistFactory.createTestPsychiatrist("physio@test.com", "ValidPassword123!");

    @Test
    public void givenNoSessions_whenGetIndibaStats_thenReturnZeroStats() {
        when(indibaSessionRepository.findAllByPatientId(any(PatientId.class))).thenReturn(List.of());

        IndibaSessionStats stats = service.getIndibaSessionStats(new PatientId(1));

        assertEquals(0, stats.totalSessions());
        assertEquals(0.0, stats.avgDurationMinutes());
        assertNull(stats.mostTreatedArea());
        assertNull(stats.avgCapacitiveIntensity());
        assertNull(stats.avgResistiveIntensity());
        assertTrue(stats.modeDistribution().isEmpty());
    }

    @Test
    public void givenOneCapacitiveSession_whenGetIndibaStats_thenCapacitiveStatsPresent() {
        Date begin = new Date(System.currentTimeMillis() - 3_600_000);
        Date end = new Date(System.currentTimeMillis());
        IndibaSession session = new IndibaSession(
                PATIENT, begin, end, "Shoulder", IndibaSessionModes.CAPACITIVE, 5.0f, null, PHYSIO, "obs");

        when(indibaSessionRepository.findAllByPatientId(any(PatientId.class))).thenReturn(List.of(session));

        IndibaSessionStats stats = service.getIndibaSessionStats(new PatientId(1));

        assertEquals(1, stats.totalSessions());
        assertTrue(stats.avgDurationMinutes() > 0);
        assertEquals("Shoulder", stats.mostTreatedArea());
        assertEquals(5.0, stats.avgCapacitiveIntensity());
        assertNull(stats.avgResistiveIntensity());
        assertEquals(1L, stats.modeDistribution().get("CAPACITIVE"));
    }

    @Test
    public void givenOneResistiveSession_whenGetIndibaStats_thenResistiveStatsPresent() {
        Date begin = new Date(System.currentTimeMillis() - 1_800_000);
        Date end = new Date(System.currentTimeMillis());
        IndibaSession session = new IndibaSession(
                PATIENT, begin, end, "Knee", IndibaSessionModes.RESISTIVE, null, 3.0f, PHYSIO, "obs");

        when(indibaSessionRepository.findAllByPatientId(any(PatientId.class))).thenReturn(List.of(session));

        IndibaSessionStats stats = service.getIndibaSessionStats(new PatientId(1));

        assertEquals(1, stats.totalSessions());
        assertEquals("Knee", stats.mostTreatedArea());
        assertNull(stats.avgCapacitiveIntensity());
        assertEquals(3.0, stats.avgResistiveIntensity());
        assertEquals(1L, stats.modeDistribution().get("RESISTIVE"));
    }

    @Test
    public void givenMultipleSessions_whenGetIndibaStats_thenCorrectAggregates() {
        Date begin = new Date(System.currentTimeMillis() - 3_600_000);
        Date end = new Date(System.currentTimeMillis());
        IndibaSession session1 = new IndibaSession(
                PATIENT, begin, end, "Lower Back", IndibaSessionModes.CAPACITIVE, 5.0f, null, PHYSIO, "obs");
        IndibaSession session2 = new IndibaSession(
                PATIENT, begin, end, "Lower Back", IndibaSessionModes.RESISTIVE, null, 3.0f, PHYSIO, "obs");
        IndibaSession session3 = new IndibaSession(
                PATIENT, begin, end, "Shoulder", IndibaSessionModes.DUAL, 4.0f, 2.0f, PHYSIO, "obs");

        when(indibaSessionRepository.findAllByPatientId(any(PatientId.class)))
                .thenReturn(List.of(session1, session2, session3));

        IndibaSessionStats stats = service.getIndibaSessionStats(new PatientId(1));

        assertEquals(3, stats.totalSessions());
        assertEquals("Lower Back", stats.mostTreatedArea());
        assertEquals(4.5, stats.avgCapacitiveIntensity(), 0.001);
        assertEquals(2.5, stats.avgResistiveIntensity(), 0.001);
        assertEquals(3, stats.modeDistribution().size());
        assertEquals(1L, stats.modeDistribution().get("CAPACITIVE"));
        assertEquals(1L, stats.modeDistribution().get("RESISTIVE"));
        assertEquals(1L, stats.modeDistribution().get("DUAL"));
    }
}
