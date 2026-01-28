package com.tfg.adapter.out.persistence.indiba;

import com.tfg.adapter.out.persistence.patient.PatientJpaEntity;
import com.tfg.adapter.out.persistence.patient.PatientJpaMapper;
import com.tfg.indiba.IndibaSession;

public class IndibaJpaMapper {

    public static IndibaJpaEntity toJpaEntity(PatientJpaEntity patientJpaEntity, IndibaSession indibaSession) {
        IndibaJpaEntity entity = new IndibaJpaEntity();
        entity.setId(indibaSession.getId().value());
        entity.setPatient(patientJpaEntity);
        entity.setBeginSession(indibaSession.getBeginSession());
        entity.setEndSession(indibaSession.getEndSession());
        entity.setTreatedArea(indibaSession.getTreatedArea());
        entity.setMode(indibaSession.getMode());
        entity.setIntensity(indibaSession.getIntensity());
        entity.setObjective(indibaSession.getObjective());
        entity.setPhysiotherapist(indibaSession.getPhysiotherapist());
        entity.setObservations(indibaSession.getObservations());
        return entity;
    }

    public static IndibaSession toModelEntity(IndibaJpaEntity entity) {
        return new IndibaSession(
                new com.tfg.indiba.IndibaSessionId(entity.getId()),
                PatientJpaMapper.toModelEntity(entity.getPatient()),
                entity.getBeginSession(),
                entity.getEndSession(),
                entity.getTreatedArea(),
                entity.getMode(),
                entity.getIntensity(),
                entity.getObjective(),
                entity.getPhysiotherapist(),
                entity.getObservations()
        );
    }

    public static java.util.List<IndibaSession> toModelEntities(java.util.List<IndibaJpaEntity> entities) {
        java.util.List<IndibaSession> sessions = new java.util.ArrayList<>();
        for (IndibaJpaEntity entity : entities) {
            sessions.add(toModelEntity(entity));
        }
        return sessions;
    }
}
