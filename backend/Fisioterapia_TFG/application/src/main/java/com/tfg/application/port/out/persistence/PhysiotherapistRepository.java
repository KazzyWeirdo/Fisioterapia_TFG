package com.tfg.application.port.out.persistence;

import com.tfg.model.physiotherapist.Physiotherapist;
import com.tfg.model.physiotherapist.PhysiotherapistEmail;
import com.tfg.model.physiotherapist.PhysiotherapistId;

import java.util.Optional;

public interface PhysiotherapistRepository {

    void save(Physiotherapist psychiatrist);

    void deleteAll();

    Optional<Physiotherapist> findById(PhysiotherapistId id);

    Optional<Physiotherapist> findByEmail(PhysiotherapistEmail email);

    void updatePassword(PhysiotherapistId id, String encodedPassword);
}
