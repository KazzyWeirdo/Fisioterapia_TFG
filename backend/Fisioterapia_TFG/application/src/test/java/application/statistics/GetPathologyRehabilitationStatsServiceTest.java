package application.statistics;

import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.application.service.statistics.GetPathologyRehabilitationStatsService;
import com.tfg.model.statistics.PathologyRehabilitationStats;
import org.junit.jupiter.api.Test;

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
}
