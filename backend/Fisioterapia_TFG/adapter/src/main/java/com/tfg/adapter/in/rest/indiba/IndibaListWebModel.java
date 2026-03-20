package com.tfg.adapter.in.rest.indiba;

import com.tfg.indiba.IndibaSession;

import java.util.Date;

public record IndibaListWebModel(
        String patientId,
        Date date
) {
    static IndibaListWebModel fromDomainModel(IndibaSession indibaSession) {
       return new IndibaListWebModel(
               String.valueOf(indibaSession.getPatient().getId()),
               indibaSession.getBeginSession()
       );
    }
}
