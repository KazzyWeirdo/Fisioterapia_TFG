package com.tfg.adapter.out.persistence.psychiatrist;

import com.tfg.port.out.persistence.PsychiatristRepository;
import com.tfg.psychiatrist.Psychiatrist;
import com.tfg.psychiatrist.PsychiatristEmail;
import com.tfg.psychiatrist.PsychiatristId;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PsychiatristJpaRepository implements PsychiatristRepository {

    private final PsychiatristJpaDataRepository psychiatristJpaDataRepository;

    public PsychiatristJpaRepository (PsychiatristJpaDataRepository psychiatristJpaDataRepository) {
        this.psychiatristJpaDataRepository = psychiatristJpaDataRepository;
    }

    @Override
    public void save(Psychiatrist psychiatrist) {
        PsychiatristJpaEntity psychiatristJpaEntity = PsychiatristJpaMapper.toJpaEntity(psychiatrist);
        psychiatristJpaDataRepository.save(psychiatristJpaEntity);
    }

    @Override
    public void deleteAll() {
        psychiatristJpaDataRepository.deleteAll();
    }

    @Override
    public Optional<Psychiatrist> findById(PsychiatristId id) {
        return psychiatristJpaDataRepository.findById(id.value())
                .map(PsychiatristJpaMapper::toModelEntity);
    }

    @Override
    public Optional<Psychiatrist> findByEmail(PsychiatristEmail email) {
        return psychiatristJpaDataRepository.findByEmail(email.value())
                .map(PsychiatristJpaMapper::toModelEntity);
    }
}
