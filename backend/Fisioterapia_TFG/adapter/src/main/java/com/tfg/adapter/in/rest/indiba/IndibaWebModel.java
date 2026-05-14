package com.tfg.adapter.in.rest.indiba;

import java.util.Date;

public record IndibaWebModel(
        int id,
        int patiendId,
        Date beginSession,
        Date endSession,
        String treatedArea,
        String mode,
        Float capacitiveIntensity,
        Float resistiveIntensity,
        String objective,
        String physiotherapistName,
        String physiotherapistSurname,
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
                indibaSession.getCapacitiveIntensity(),
                indibaSession.getResistiveIntensity(),
                indibaSession.getObjective(),
                indibaSession.getPhysiotherapist().getName(),
                indibaSession.getPhysiotherapist().getSurname(),
                indibaSession.getObservations()
        );
    }
}
