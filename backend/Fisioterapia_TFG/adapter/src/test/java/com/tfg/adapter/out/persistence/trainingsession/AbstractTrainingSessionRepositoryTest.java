package com.tfg.adapter.out.persistence.trainingsession;

import com.tfg.model.patient.PatientFactory;
import com.tfg.model.trainingsession.ExerciseFactory;
import com.tfg.model.trainingsession.ExerciseSetFactory;
import com.tfg.model.trainingsession.TrainingSessionFactory;
import com.tfg.patient.Patient;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.TrainingSessionRepository;
import com.tfg.trainingsession.Exercise;
import com.tfg.trainingsession.ExerciseSet;
import com.tfg.trainingsession.TrainingSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractTrainingSessionRepositoryTest {
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
    }

    @Test
    public void givenAnExistingPatient_whenFindByPatient_thenReturnTrainingSessions() {
        trainingSessionRepository.save(testTrainingSession1);
        trainingSessionRepository.save(testTrainingSession2);

        var trainingSessions = trainingSessionRepository.findAllByPatientId(testPatient.getId());

        assertThat(trainingSessions).isNotEmpty();
        assertThat(trainingSessions.size()).isEqualTo(2);
    }

    @Test
    public void givenNoExistingPatient_whenFindByPatient_thenReturnEmptyList() {
        var trainingSessions = trainingSessionRepository.findAllByPatientId(testPatient.getId());

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

        List<TrainingSession> trainingSession = trainingSessionRepository.findAllByPatientId(testPatient.getId());

        assertThat(trainingSession).isEmpty();
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
}
