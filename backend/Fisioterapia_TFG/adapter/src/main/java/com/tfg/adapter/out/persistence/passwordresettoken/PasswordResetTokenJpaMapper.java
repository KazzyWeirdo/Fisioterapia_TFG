package com.tfg.adapter.out.persistence.passwordresettoken;

import com.tfg.adapter.out.persistence.physiotherapist.PhysiotherapistJpaEntity;
import com.tfg.adapter.out.persistence.physiotherapist.PhysiotherapistJpaMapper;

public class PasswordResetTokenJpaMapper {
    public static PasswordResetTokenEntity toJpaEntity(com.tfg.passwordresettoken.PasswordResetToken token, PhysiotherapistJpaEntity physiotherapistJpaEntity) {
        PasswordResetTokenEntity entity = new PasswordResetTokenEntity();
        entity.setToken(token.token());
        entity.setPhysiotherapist(physiotherapistJpaEntity);
        entity.setExpiresAt(token.expiresAt());
        return entity;
    }

    public static com.tfg.passwordresettoken.PasswordResetToken toDomainEntity(PasswordResetTokenEntity jpaEntity) {
        return new com.tfg.passwordresettoken.PasswordResetToken(
                jpaEntity.getToken(),
                PhysiotherapistJpaMapper.toModelEntity(jpaEntity.getPhysiotherapist()),
                jpaEntity.getExpiresAt()
        );
    }
}
