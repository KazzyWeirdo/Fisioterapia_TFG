package application.exercisetemplate;

import com.tfg.model.trainingsession.ExerciseFactory;
import com.tfg.model.trainingsession.ExerciseSetFactory;
import com.tfg.model.trainingsession.ExerciseTemplateFactory;
import com.tfg.port.out.persistence.ExerciseTemplateRepository;
import com.tfg.service.exercisetemplate.CreateExerciseTemplateService;
import com.tfg.trainingsession.Exercise;
import com.tfg.trainingsession.ExerciseSet;
import com.tfg.trainingsession.ExerciseTemplate;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CreateExerciseTemplateServiceTest {

    private final ExerciseTemplateRepository exerciseTemplateRepository = mock(ExerciseTemplateRepository.class);
    private final CreateExerciseTemplateService exerciseTemplateService = new CreateExerciseTemplateService(exerciseTemplateRepository);

    private static final ExerciseSet TEST_SET = ExerciseSetFactory.createTestExerciseSet(1);
    private static final Exercise TEST_EXERCISE = ExerciseFactory.createTestExerciseWithExerciseSets("Squat", TEST_SET);
    private static final ExerciseTemplate TEST_TEMPLATE = ExerciseTemplateFactory.createTestExerciseTemplateWithExercises("Leg Day", TEST_EXERCISE);

    @Test
    public void givenNewExerciseTemplate_createExerciseTemplate(){
        exerciseTemplateService.createExerciseTemplate(TEST_TEMPLATE);

        verify(exerciseTemplateRepository).save(argThat(template ->
                template.getName().equals("Leg Day") &&
                template.getExercises().size() == 1 &&
                template.getExercises().get(0).getName().equals("Squat")
        ));
    }
}
