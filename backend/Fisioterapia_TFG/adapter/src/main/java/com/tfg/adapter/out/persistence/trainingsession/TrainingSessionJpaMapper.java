package com.tfg.adapter.out.persistence.trainingsession;

import com.tfg.adapter.out.persistence.exercisetemplate.ExerciseTemplateJpaEntity;
import com.tfg.adapter.out.persistence.exercisetemplate.ExerciseTemplateJpaMapper;
import com.tfg.adapter.out.persistence.patient.PatientJpaEntity;
import com.tfg.adapter.out.persistence.patient.PatientJpaMapper;
import com.tfg.adapter.out.persistence.physiotherapist.PhysiotherapistJpaEntity;
import com.tfg.adapter.out.persistence.physiotherapist.PhysiotherapistJpaMapper;
import com.tfg.trainingsession.TrainingSession;
import com.tfg.trainingsession.TrainingSessionId;

import java.util.ArrayList;
import java.util.List;

public class TrainingSessionJpaMapper {

        public static TrainingSessionJpaEntity toJpaEntity(TrainingSession trainingSession, PatientJpaEntity patientJpaEntity, PhysiotherapistJpaEntity physiotherapistJpaEntity) {
                TrainingSessionJpaEntity entity = new TrainingSessionJpaEntity();
                entity.setId(trainingSession.getId().value());
                entity.setPatient(patientJpaEntity);
                entity.setPhysiotherapist(physiotherapistJpaEntity);
                entity.setStartDateTime(trainingSession.getStartDateTime());
                entity.setEndDateTime(trainingSession.getEndDateTime());
                trainingSession.getExerciseTemplates().forEach(template -> {
                        ExerciseTemplateJpaEntity templateJpaEntity = ExerciseTemplateJpaMapper.toJpaEntity(template);
                        templateJpaEntity.setTrainingSession(entity);
                        entity.addExerciseTemplate(templateJpaEntity);
                });
                return entity;
        }

        public static TrainingSession toModelEntity(TrainingSessionJpaEntity trainingSession) {
                TrainingSession model = new TrainingSession(
                        new TrainingSessionId(trainingSession.getId()),
                        PatientJpaMapper.toModelEntity(trainingSession.getPatient()),
                        trainingSession.getStartDateTime(),
                        trainingSession.getEndDateTime(),
                        PhysiotherapistJpaMapper.toModelEntity(trainingSession.getPhysiotherapist()),
                        new ArrayList<>()
                );
                trainingSession.getExerciseTemplates().forEach(template -> model.addExerciseTemplate(ExerciseTemplateJpaMapper.toModel(template)));
                return model;
        }
}
