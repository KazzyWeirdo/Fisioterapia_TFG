package com.tfg.adapter.out.persistence.trainingsession;

import com.tfg.adapter.out.persistence.patient.PatientJpaEntity;
import com.tfg.adapter.out.persistence.patient.PatientJpaMapper;
import com.tfg.trainingsession.TrainingSession;
import com.tfg.trainingsession.TrainingSessionId;

import java.util.ArrayList;
import java.util.List;

public class TrainingSessionJpaMapper {

        public static TrainingSessionJpaEntity toJpaEntity(TrainingSession trainingSession, PatientJpaEntity patientJpaEntity) {
                TrainingSessionJpaEntity entity = new TrainingSessionJpaEntity();
                entity.setId(trainingSession.getId().value());
                entity.setPatient(patientJpaEntity);
                entity.setDate(trainingSession.getDate());
                entity.setExercises(trainingSession.getExercises().stream()
                        .map(exercise -> ExercisesJpaMapper.toJpaEntity(exercise, entity))
                        .toList());
                return entity;
        }

        public static TrainingSession toModelEntity(TrainingSessionJpaEntity trainingSession) {
                TrainingSession model = new TrainingSession(
                        new TrainingSessionId(trainingSession.getId()),
                        PatientJpaMapper.toModelEntity(trainingSession.getPatient()),
                        trainingSession.getDate(),
                        new ArrayList<>()
                );
                trainingSession.getExercises().forEach(exercise -> model.addExercise(ExercisesJpaMapper.toModelEntity(exercise)));
                return model;
        }
}
