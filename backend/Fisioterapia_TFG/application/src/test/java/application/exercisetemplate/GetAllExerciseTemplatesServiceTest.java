package application.exercisetemplate;

import com.tfg.port.out.persistence.ExerciseTemplateRepository;
import com.tfg.service.exercisetemplate.GetAllExerciseTemplatesService;
import com.tfg.trainingsession.ExerciseTemplate;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetAllExerciseTemplatesServiceTest {

    private final ExerciseTemplateRepository repository = mock(ExerciseTemplateRepository.class);
    private final GetAllExerciseTemplatesService service = new GetAllExerciseTemplatesService(repository);

    @Test
    void givenTemplates_whenGetAll_thenReturnsAll() {
        List<ExerciseTemplate> templates = List.of(
                new ExerciseTemplate("Protocol A"),
                new ExerciseTemplate("Protocol B")
        );
        when(repository.findAll()).thenReturn(templates);
        assertThat(service.getAllExerciseTemplates()).hasSize(2);
    }
}
