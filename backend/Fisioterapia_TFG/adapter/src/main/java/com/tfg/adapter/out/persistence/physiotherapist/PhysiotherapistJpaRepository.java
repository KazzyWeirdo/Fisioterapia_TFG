package com.tfg.adapter.out.persistence.physiotherapist;

import com.tfg.application.port.out.persistence.PhysiotherapistRepository;
import com.tfg.model.physiotherapist.Physiotherapist;
import com.tfg.model.physiotherapist.PhysiotherapistEmail;
import com.tfg.model.physiotherapist.PhysiotherapistId;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public class PhysiotherapistJpaRepository implements PhysiotherapistRepository {

    private final PhysiotherapistJpaDataRepository psychiatristJpaDataRepository;
    private final RoleJpaDataRepository roleJpaDataRepository;

    public PhysiotherapistJpaRepository(PhysiotherapistJpaDataRepository psychiatristJpaDataRepository,
                                        RoleJpaDataRepository roleJpaDataRepository) {
        this.psychiatristJpaDataRepository = psychiatristJpaDataRepository;
        this.roleJpaDataRepository = roleJpaDataRepository;
    }

    @Override
    @Transactional
    public void save(Physiotherapist psychiatrist) {
        PhysiotherapistJpaEntity psychiatristJpaEntity = PhysiotherapistJpaMapper.toJpaEntity(psychiatrist);
        Set<RoleJpaEntity> roleEntities = psychiatrist.getRoles().stream()
                .map(role -> roleJpaDataRepository.findByName(role).orElseThrow())
                .collect(java.util.stream.Collectors.toSet());
        psychiatristJpaEntity.setRoles(roleEntities);
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

    @Override
    public void updatePassword(PhysiotherapistId id, String encodedPassword) {
        Optional<PhysiotherapistJpaEntity> optionalEntity = psychiatristJpaDataRepository.findById(id.value());
        if (optionalEntity.isPresent()) {
            PhysiotherapistJpaEntity entity = optionalEntity.get();
            entity.setPassword(encodedPassword);
            psychiatristJpaDataRepository.save(entity);
        }
    }
}
