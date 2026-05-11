package application.statistics;

import com.tfg.model.patient.PatientId;
import com.tfg.application.port.out.persistence.IndibaSessionRepository;
import com.tfg.application.service.statistics.GetIndibaSessionStatsService;
import com.tfg.model.statistics.IndibaSessionStats;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetIndibaSessionStatsServiceTest {

    private final IndibaSessionRepository indibaSessionRepository = mock(IndibaSessionRepository.class);
    private final GetIndibaSessionStatsService service =
            new GetIndibaSessionStatsService(indibaSessionRepository);

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
}
