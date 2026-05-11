package com.tfg.adapter.out.persistence.indiba;

import com.tfg.adapter.out.persistence.patient.PatientJpaEntity;
import com.tfg.adapter.out.persistence.patient.PatientJpaMapper;
import com.tfg.adapter.out.persistence.physiotherapist.PhysiotherapistJpaEntity;
import com.tfg.adapter.out.persistence.physiotherapist.PhysiotherapistJpaMapper;
import com.tfg.model.indiba.IndibaSession;
import com.tfg.model.indiba.IndibaSessionId;

public class IndibaJpaMapper {

    public static IndibaJpaEntity toJpaEntity(PatientJpaEntity patientJpaEntity, PhysiotherapistJpaEntity physiotherapistJpaEntity, IndibaSession indibaSession) {
        IndibaJpaEntity entity = new IndibaJpaEntity();
        entity.setId(indibaSession.getId().value());
        entity.setPatient(patientJpaEntity);
        entity.setBeginSession(indibaSession.getBeginSession());
        entity.setEndSession(indibaSession.getEndSession());
        entity.setTreatedArea(indibaSession.getTreatedArea());
        entity.setMode(indibaSession.getMode());
        entity.setCapacitiveIntensity(indibaSession.getCapacitiveIntensity());
        entity.setResistiveIntensity(indibaSession.getResistiveIntensity());
        entity.setPhysiotherapist(physiotherapistJpaEntity);
        entity.setObservations(indibaSession.getObservations());
        return entity;
    }

    public static IndibaSession toModelEntity(IndibaJpaEntity entity) {
        return new IndibaSession(
                new IndibaSessionId(entity.getId()),
                PatientJpaMapper.toModelEntity(entity.getPatient()),
                entity.getBeginSession(),
                entity.getEndSession(),
                entity.getTreatedArea(),
                entity.getMode(),
                entity.getCapacitiveIntensity(),
                entity.getResistiveIntensity(),
                PhysiotherapistJpaMapper.toModelEntity(entity.getPhysiotherapist()),
                entity.getObservations()
        );
    }
}
