package application.exercisetemplate;

import com.tfg.application.port.out.persistence.ExerciseTemplateRepository;
import com.tfg.application.service.exercisetemplate.GetExerciseTemplateByIdService;
import com.tfg.model.trainingsession.ExerciseTemplate;
import com.tfg.model.trainingsession.ExerciseTemplateFactory;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetExerciseTemplateByIdServiceTest {

    private final ExerciseTemplateRepository repository = mock(ExerciseTemplateRepository.class);
    private final GetExerciseTemplateByIdService service = new GetExerciseTemplateByIdService(repository);

    private static final ExerciseTemplate TEST_TEMPLATE = ExerciseTemplateFactory.createTestExerciseTemplate("Leg Day");

    @Test
    void givenExistingId_getById_returnsTemplate() {
        when(repository.findById(1)).thenReturn(Optional.of(TEST_TEMPLATE));

        Optional<ExerciseTemplate> result = service.getById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Leg Day");
    }

    @Test
    void givenNonExistingId_getById_returnsEmpty() {
        when(repository.findById(999)).thenReturn(Optional.empty());

        Optional<ExerciseTemplate> result = service.getById(999);

        assertThat(result).isEmpty();
    }
}
