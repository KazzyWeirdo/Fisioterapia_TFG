package com.tfg.adapter.out.persistence.exercisetemplate;

import com.tfg.adapter.out.persistence.BaseRepositoryIT;
import com.tfg.model.trainingsession.ExerciseFactory;
import com.tfg.model.trainingsession.ExerciseSetFactory;
import com.tfg.model.trainingsession.ExerciseTemplateFactory;
import com.tfg.application.port.out.persistence.ExerciseTemplateRepository;
import com.tfg.model.trainingsession.Exercise;
import com.tfg.model.trainingsession.ExerciseSet;
import com.tfg.model.trainingsession.ExerciseTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractExerciseTemplateRepositoryTest extends BaseRepositoryIT {

    private ExerciseTemplate testTemplate1;
    private ExerciseTemplate testTemplate2;

    @Autowired
    public ExerciseTemplateRepository exerciseTemplateRepository;

    @BeforeEach
    void setUp() {
        ExerciseSet exerciseSet = ExerciseSetFactory.createTestExerciseSet(1);
        Exercise exercise = ExerciseFactory.createTestExerciseWithExerciseSets("Squat", exerciseSet);
        testTemplate1 = ExerciseTemplateFactory.createTestExerciseTemplateWithExercises("Leg Day", exercise);
        testTemplate2 = ExerciseTemplateFactory.createTestExerciseTemplate("Upper Body");
    }

    @AfterEach
    void tearDown() {
        exerciseTemplateRepository.deleteAll();
    }

    @Test
    public void givenExerciseTemplate_whenSave_thenTemplateIsSaved() {
        exerciseTemplateRepository.save(testTemplate1);

        List<ExerciseTemplate> result = exerciseTemplateRepository.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo(testTemplate1.getName());
        assertThat(result.get(0).getExercises()).hasSize(1);
        assertThat(result.get(0).getExercises().get(0).getName()).isEqualTo("Squat");
    }

    @Test
    public void givenNoExerciseTemplates_whenFindAll_thenReturnEmptyList() {
        List<ExerciseTemplate> result = exerciseTemplateRepository.findAll();

        assertThat(result).isEmpty();
    }

    @Test
    public void givenMultipleExerciseTemplates_whenFindAll_thenReturnAll() {
        exerciseTemplateRepository.save(testTemplate1);
        exerciseTemplateRepository.save(testTemplate2);

        List<ExerciseTemplate> result = exerciseTemplateRepository.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(ExerciseTemplate::getName)
                .containsExactlyInAnyOrder(testTemplate1.getName(), testTemplate2.getName());
    }

    @Test
    public void givenExerciseTemplates_whenDeleteAll_thenAllRemoved() {
        exerciseTemplateRepository.save(testTemplate1);
        exerciseTemplateRepository.save(testTemplate2);

        exerciseTemplateRepository.deleteAll();

        List<ExerciseTemplate> result = exerciseTemplateRepository.findAll();

        assertThat(result).isEmpty();
    }
}
