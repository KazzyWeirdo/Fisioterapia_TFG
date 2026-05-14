package com.tfg.port.out.persistence;

import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.physiotherapist.PhysiotherapistEmail;
import com.tfg.physiotherapist.PhysiotherapistId;

import java.util.Optional;

public interface PhysiotherapistRepository {

    void save(Physiotherapist psychiatrist);

    void deleteAll();

    Optional<Physiotherapist> findById(PhysiotherapistId id);

    Optional<Physiotherapist> findByEmail(PhysiotherapistEmail email);

    void updatePassword(PhysiotherapistId id, String encodedPassword);
}
