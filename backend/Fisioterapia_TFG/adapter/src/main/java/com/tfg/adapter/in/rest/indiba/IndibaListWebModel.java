package com.tfg.adapter.in.rest.indiba;

import com.tfg.indiba.IndibaSession;

import java.util.Date;

public record IndibaListWebModel(
        int id,
        Date date
) {
    static IndibaListWebModel fromDomainModel(IndibaSession indibaSession) {
       return new IndibaListWebModel(
               indibaSession.getId().value(),
               indibaSession.getBeginSession()
       );
    }
}
