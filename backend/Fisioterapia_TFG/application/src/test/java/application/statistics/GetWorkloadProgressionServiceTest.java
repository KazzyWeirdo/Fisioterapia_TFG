package application.statistics;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.TrainingSessionRepository;
import com.tfg.service.statistics.GetWorkloadProgressionService;
import com.tfg.statistics.WorkloadProgression;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class GetWorkloadProgressionServiceTest {

    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final TrainingSessionRepository trainingSessionRepository = mock(TrainingSessionRepository.class);
    private final GetWorkloadProgressionService service = new GetWorkloadProgressionService(trainingSessionRepository, patientRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");

    @Test
    void calculateProgression_ShouldReturnWorkloadProgression_WhenDataExists() {
        PatientId patientId = new PatientId(1);
        String exerciseName = "Squat";

        when(patientRepository.findById(new PatientId(1)))
                .thenReturn(Optional.of(TEST_PATIENT));


        List<Object[]> rawData = List.of(
                new Object[]{Date.valueOf("2023-10-01"), 100.0},
                new Object[]{Date.valueOf("2023-10-08"), 150.0}
        );
        when(trainingSessionRepository.calculateVolumeProgression(patientId, exerciseName)).thenReturn(rawData);

        List<WorkloadProgression> result = service.calculateProgression(patientId, exerciseName);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).sessionDate()).isEqualTo(LocalDate.of(2023, 10, 1));
        assertThat(result.get(0).workload()).isEqualTo(100.0);
        assertThat(result.get(1).sessionDate()).isEqualTo(LocalDate.of(2023, 10, 8));
        assertThat(result.get(1).workload()).isEqualTo(150.0);

        verify(patientRepository, times(1)).findById(patientId);
        verify(trainingSessionRepository, times(1)).calculateVolumeProgression(patientId, exerciseName);
    }

    @Test
    void calculateProgression_ShouldThrowInvalidIdException_WhenPatientDoesNotExist() {
        PatientId patientId = new PatientId(1);
        String exerciseName = "Squat";

        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.calculateProgression(patientId, exerciseName))
                .isInstanceOf(InvalidIdException.class);

        verify(patientRepository, times(1)).findById(patientId);
        verifyNoInteractions(trainingSessionRepository);
    }

    @Test
    void calculateProgression_ShouldReturnEmptyList_WhenNoDataExists() {
        PatientId patientId = new PatientId(1);
        String exerciseName = "Squat";

        when(patientRepository.findById(new PatientId(1)))
                .thenReturn(Optional.of(TEST_PATIENT));

        when(trainingSessionRepository.calculateVolumeProgression(patientId, exerciseName)).thenReturn(List.of());

        List<WorkloadProgression> result = service.calculateProgression(patientId, exerciseName);

        assertThat(result).isEmpty();

        verify(patientRepository, times(1)).findById(patientId);
        verify(trainingSessionRepository, times(1)).calculateVolumeProgression(patientId, exerciseName);
    }
}