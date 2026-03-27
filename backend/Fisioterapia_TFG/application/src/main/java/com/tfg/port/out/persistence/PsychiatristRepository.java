package com.tfg.port.out.persistence;

import com.tfg.psychiatrist.Psychiatrist;
import com.tfg.psychiatrist.PsychiatristEmail;
import com.tfg.psychiatrist.PsychiatristId;

import java.util.Optional;

public interface PsychiatristRepository {

    void save(Psychiatrist psychiatrist);

    void deleteAll();

    Optional<Psychiatrist> findById(PsychiatristId id);

    Optional<Psychiatrist> findByEmail(PsychiatristEmail email);
}
