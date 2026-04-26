package com.tfg.adapter.out.persistence.trainingsession;

import com.tfg.model.patient.PatientFactory;
import com.tfg.model.trainingsession.ExerciseFactory;
import com.tfg.model.trainingsession.ExerciseSetFactory;
import com.tfg.model.trainingsession.TrainingSessionFactory;
import com.tfg.patient.Patient;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.TrainingSessionSummaryElement;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.TrainingSessionRepository;
import com.tfg.trainingsession.Exercise;
import com.tfg.trainingsession.ExerciseSet;
import com.tfg.trainingsession.TrainingSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.tfg.adapter.out.persistence.BaseRepositoryIT;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractTrainingSessionRepositoryTest extends BaseRepositoryIT {
    private Patient testPatient;
    private TrainingSession testTrainingSession1;
    private TrainingSession testTrainingSession2;
    private Exercise exercise1;
    private ExerciseSet exerciseSet1;

    @Autowired
    public TrainingSessionRepository trainingSessionRepository;

    @Autowired
    public PatientRepository patientRepository;

    @BeforeEach
    void setUp() {
        testPatient = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");

        patientRepository.save(testPatient);

        exerciseSet1 = ExerciseSetFactory.createTestExerciseSet(5);
        exercise1 = ExerciseFactory.createTestExerciseWithExerciseSets("Test Exercise", exerciseSet1);
        testTrainingSession1 = TrainingSessionFactory.createTestTrainingSessionWithExercises(testPatient, LocalDate.of(2024, 6, 1), exercise1);
        testTrainingSession2 = TrainingSessionFactory.createTestTrainingSessionWithExercises(testPatient, LocalDate.of(2024, 7, 2), exercise1);
    }

    @AfterEach
    void tearDown() {
        trainingSessionRepository.deleteAll();
        patientRepository.deleteAll();
    }

    @Test
    public void givenAnExistingPatient_whenFindByPatient_thenReturnTrainingSessions() {
        trainingSessionRepository.save(testTrainingSession1);
        trainingSessionRepository.save(testTrainingSession2);

        PageQuery query = new PageQuery(0, 10);

        PagedResponse<TrainingSessionSummaryElement> response = trainingSessionRepository.findAllByPatientId(query, testPatient.getId());

        List<TrainingSessionSummaryElement> trainingSessions = response.content();

        assertThat(response.totalElements()).isEqualTo(2);
        assertThat(response.totalPages()).isEqualTo(1);
        assertThat(response.pageNumber()).isEqualTo(0);
        assertThat(response.isLast()).isTrue();

        assertThat(trainingSessions).hasSize(2);
        assertThat(trainingSessions.get(0).id()).isEqualTo(testTrainingSession1.getId().value());
        assertThat(trainingSessions.get(0).date()).isEqualTo(testTrainingSession1.getDate());
        assertThat(trainingSessions.get(1).id()).isEqualTo(testTrainingSession2.getId().value());
        assertThat(trainingSessions.get(1).date()).isEqualTo(testTrainingSession2.getDate());
    }

    @Test
    public void givenNoExistingPatient_whenFindByPatient_thenReturnEmptyList() {
        PageQuery query = new PageQuery(0, 10);

        PagedResponse<TrainingSessionSummaryElement> response = trainingSessionRepository.findAllByPatientId(query, testPatient.getId());

        List<TrainingSessionSummaryElement> trainingSessions = response.content();

        assertThat(trainingSessions).isEmpty();
    }

    @Test
    public void givenAnExistingTrainingSession_whenFindById_returnTrainingSession() {
        trainingSessionRepository.save(testTrainingSession1);

        var trainingSession = trainingSessionRepository.findById(testTrainingSession1.getId());

        assertThat(trainingSession).isPresent();
        assertThat(trainingSession.get().getDate()).isEqualTo(testTrainingSession1.getDate());
    }

    @Test
    public void givenNoExistingTrainingSession_whenFindById_thenReturnEmpty() {
        var trainingSession = trainingSessionRepository.findById(testTrainingSession1.getId());

        assertThat(trainingSession).isNotPresent();
    }

    @Test
    public void givenExistingTrainingSession_whenDelete_thenRemoveTrainingSession() {
        trainingSessionRepository.save(testTrainingSession1);
        trainingSessionRepository.save(testTrainingSession2);

        trainingSessionRepository.deleteAll();

        PageQuery query = new PageQuery(0, 10);

        PagedResponse<TrainingSessionSummaryElement> response = trainingSessionRepository.findAllByPatientId(query, testPatient.getId());

        List<TrainingSessionSummaryElement> trainingSessions = response.content();

        assertThat(trainingSessions).isEmpty();
    }

    @Test
    public void givenExistingTrainingSession_whenSave_thenTrainingSessionIsSaved() {
        trainingSessionRepository.save(testTrainingSession1);

        Optional<TrainingSession> retrievedTrainingSession = trainingSessionRepository.findById(testTrainingSession1.getId());

        assertThat(retrievedTrainingSession).isPresent();
        assertThat(retrievedTrainingSession.get().getDate()).isEqualTo(testTrainingSession1.getDate());
    }

    @Test
    public void givenExistingTrainingSession_whenFindByYear_returnList() {
        trainingSessionRepository.save(testTrainingSession1);
        trainingSessionRepository.save(testTrainingSession2);

        List<Object[]> trainingSessions = trainingSessionRepository.countSessionByMonth(testPatient.getId(), testTrainingSession1.getDate().getYear());

        assertThat(trainingSessions).isNotEmpty();
        assertThat(trainingSessions.size()).isEqualTo(2);
    }

    @Test
    public void givenNoTrainingSessions_whenFindByYear_returnEmptyList() {
        List<Object[]> trainingSessions = trainingSessionRepository.countSessionByMonth(testPatient.getId(), testTrainingSession1.getDate().getYear());

        assertThat(trainingSessions).isEmpty();
    }

    @Test
    public void givenExistingTrainingSession_whenCalculateVolumeProgression_thenReturnList() {
        trainingSessionRepository.save(testTrainingSession1);
        trainingSessionRepository.save(testTrainingSession2);

        List<Object[]> progression = trainingSessionRepository.calculateVolumeProgression(testPatient.getId(), "Test Exercise");

        assertThat(progression).isNotEmpty();
        assertThat(progression.size()).isEqualTo(1);
    }

    @Test
    public void givenNoTrainingSessions_whenCalculateVolumeProgression_thenReturnEmptyList() {
        List<Object[]> progression = trainingSessionRepository.calculateVolumeProgression(testPatient.getId(), "Test Exercise");

        assertThat(progression).isEmpty();
    }

    @Test
    public void givenExistingTrainingSessions_whenFindAllForExport_returnAll() {
        trainingSessionRepository.save(testTrainingSession1);
        trainingSessionRepository.save(testTrainingSession2);

        List<TrainingSession> result = trainingSessionRepository.findAllForExport();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(s -> s.getId().value())
                .containsExactlyInAnyOrder(testTrainingSession1.getId().value(), testTrainingSession2.getId().value());
        assertThat(result).anySatisfy(session -> {
            assertThat(session.getExercises()).isNotEmpty();
            assertThat(session.getExercises().get(0).getSets()).isNotEmpty();
        });
    }

    @Test
    public void givenNoTrainingSessions_whenFindAllForExport_returnEmptyList() {
        List<TrainingSession> result = trainingSessionRepository.findAllForExport();
        assertThat(result).isEmpty();
    }
}
