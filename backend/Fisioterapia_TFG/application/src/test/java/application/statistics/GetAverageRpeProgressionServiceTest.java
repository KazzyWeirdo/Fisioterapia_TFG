package application.statistics;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.patient.Patient;
import com.tfg.model.patient.PatientId;
import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.application.port.out.persistence.TrainingSessionRepository;
import com.tfg.application.service.statistics.GetAverageRpeProgressionService;
import com.tfg.model.statistics.AverageRpeProgression;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class GetAverageRpeProgressionServiceTest {

    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final TrainingSessionRepository trainingSessionRepository = mock(TrainingSessionRepository.class);
    private final GetAverageRpeProgressionService service = new GetAverageRpeProgressionService(trainingSessionRepository, patientRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");

    @Test
    void calculateProgression_ShouldReturnAverageRpeProgression_WhenDataExists() {
        PatientId patientId = new PatientId(1);
        String exerciseName = "Squat";

        when(patientRepository.findById(new PatientId(1)))
                .thenReturn(Optional.of(TEST_PATIENT));


        List<Object[]> rawData = List.of(
                new Object[]{Date.valueOf("2023-10-01"), 100.0},
                new Object[]{Date.valueOf("2023-10-08"), 150.0}
        );
        when(trainingSessionRepository.calculateRpeProgression(patientId, exerciseName)).thenReturn(rawData);

        List<AverageRpeProgression> result = service.calculateProgression(patientId, exerciseName);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).sessionDate()).isEqualTo(LocalDate.of(2023, 10, 1));
        assertThat(result.get(0).averageRpe()).isEqualTo(100.0);
        assertThat(result.get(1).sessionDate()).isEqualTo(LocalDate.of(2023, 10, 8));
        assertThat(result.get(1).averageRpe()).isEqualTo(150.0);

        verify(patientRepository, times(1)).findById(patientId);
        verify(trainingSessionRepository, times(1)).calculateRpeProgression(patientId, exerciseName);
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

        when(trainingSessionRepository.calculateRpeProgression(patientId, exerciseName)).thenReturn(List.of());

        List<AverageRpeProgression> result = service.calculateProgression(patientId, exerciseName);

        assertThat(result).isEmpty();

        verify(patientRepository, times(1)).findById(patientId);
        verify(trainingSessionRepository, times(1)).calculateRpeProgression(patientId, exerciseName);
    }
}