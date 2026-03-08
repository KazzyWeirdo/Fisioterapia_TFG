package com.tfg.port.out.persistence;

import com.tfg.patient.PatientId;
import com.tfg.trainingsession.Exercise;
import com.tfg.trainingsession.ExerciseSet;
import com.tfg.trainingsession.TrainingSession;
import com.tfg.trainingsession.TrainingSessionId;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainingSessionRepository {

    void save(TrainingSession trainingSession);

    void saveExercise(Exercise exercise);

    void saveExerciseSet(ExerciseSet exerciseSet);

    void deleteAll();

    Optional<TrainingSession> findById (TrainingSessionId id);

    List<LocalDate> findAllByPatientId (PatientId patientId);
}
