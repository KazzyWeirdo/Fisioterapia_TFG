package com.tfg.adapter.out.persistence.passwordresettoken;

import com.tfg.adapter.out.persistence.physiotherapist.PhysiotherapistJpaEntity;
import com.tfg.adapter.out.persistence.physiotherapist.PhysiotherapistJpaMapper;
import com.tfg.passwordresettoken.PasswordResetToken;
import com.tfg.port.out.persistence.PasswordResetTokenRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PasswordResetTokenJpaRepository implements PasswordResetTokenRepository {

    private final PasswordResetTokenJpaDataRepository jpaRepository;

    public PasswordResetTokenJpaRepository(PasswordResetTokenJpaDataRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(PasswordResetToken token) {
        PhysiotherapistJpaEntity physiotherapistJpaEntity = PhysiotherapistJpaMapper.toJpaEntity(token.physio());
        jpaRepository.save(PasswordResetTokenJpaMapper.toJpaEntity(token, physiotherapistJpaEntity));
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return jpaRepository.findById(token)
                .map(e -> new PasswordResetToken(e.getToken(), PhysiotherapistJpaMapper.toModelEntity(e.getPhysiotherapist()), e.getExpiresAt()));
    }

    @Override
    public void delete(String token) {
        jpaRepository.deleteById(token);
    }
}
