package com.tfg.adapter.out.persistence.physiotherapist;

import com.tfg.port.out.persistence.PhysiotherapistRepository;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.physiotherapist.PhysiotherapistEmail;
import com.tfg.physiotherapist.PhysiotherapistId;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PhysiotherapistJpaRepository implements PhysiotherapistRepository {

    private final PhysiotherapistJpaDataRepository psychiatristJpaDataRepository;

    public PhysiotherapistJpaRepository(PhysiotherapistJpaDataRepository psychiatristJpaDataRepository) {
        this.psychiatristJpaDataRepository = psychiatristJpaDataRepository;
    }

    @Override
    @Transactional
    public void save(Physiotherapist psychiatrist) {
        PhysiotherapistJpaEntity psychiatristJpaEntity = PhysiotherapistJpaMapper.toJpaEntity(psychiatrist);
        psychiatristJpaDataRepository.save(psychiatristJpaEntity);
    }

    @Override
    public void deleteAll() {
        psychiatristJpaDataRepository.deleteAll();
    }

    @Override
    public Optional<Physiotherapist> findById(PhysiotherapistId id) {
        return psychiatristJpaDataRepository.findById(id.value())
                .map(PhysiotherapistJpaMapper::toModelEntity);
    }

    @Override
    public Optional<Physiotherapist> findByEmail(PhysiotherapistEmail email) {
        return psychiatristJpaDataRepository.findByEmail(email.value())
                .map(PhysiotherapistJpaMapper::toModelEntity);
    }
}
