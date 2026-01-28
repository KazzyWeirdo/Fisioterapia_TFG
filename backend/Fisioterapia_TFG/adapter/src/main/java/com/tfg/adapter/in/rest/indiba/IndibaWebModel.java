package com.tfg.adapter.in.rest.indiba;

import java.util.Date;

public record IndibaWebModel(
        int id,
        int patiendId,
        Date beginSession,
        Date endSession,
        String treatedArea,
        String mode,
        float intensity,
        String objective,
        String physiotherapist,
        String observations
) {
    static IndibaWebModel fromDomainModel(com.tfg.indiba.IndibaSession indibaSession) {
        return new IndibaWebModel(
                indibaSession.getId().value(),
                indibaSession.getPatient().getId().value(),
                indibaSession.getBeginSession(),
                indibaSession.getEndSession(),
                indibaSession.getTreatedArea(),
                indibaSession.getMode().toString(),
                indibaSession.getIntensity(),
                indibaSession.getObjective(),
                indibaSession.getPhysiotherapist(),
                indibaSession.getObservations()
        );
    }
}
