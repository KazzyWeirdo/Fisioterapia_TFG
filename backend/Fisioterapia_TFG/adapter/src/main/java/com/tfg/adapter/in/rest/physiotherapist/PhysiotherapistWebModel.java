package com.tfg.adapter.in.rest.physiotherapist;

import com.tfg.physiotherapist.Physiotherapist;

public record PhysiotherapistWebModel(int id, String name, String surname) {

    static PhysiotherapistWebModel fromDomainModel(Physiotherapist p) {
        return new PhysiotherapistWebModel(p.getId().value(), p.getName(), p.getSurname());
    }
}
