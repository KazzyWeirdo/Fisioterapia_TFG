package com.tfg.adapter.out.persistence.passwordresettoken;

import com.tfg.adapter.out.persistence.physiotherapist.PhysiotherapistJpaEntity;
import com.tfg.adapter.out.persistence.physiotherapist.PhysiotherapistJpaMapper;
import com.tfg.model.passwordresettoken.PasswordResetToken;

public class PasswordResetTokenJpaMapper {
    public static PasswordResetTokenEntity toJpaEntity(PasswordResetToken token, PhysiotherapistJpaEntity physiotherapistJpaEntity) {
        PasswordResetTokenEntity entity = new PasswordResetTokenEntity();
        entity.setToken(token.token());
        entity.setPhysiotherapist(physiotherapistJpaEntity);
        entity.setExpiresAt(token.expiresAt());
        return entity;
    }

    public static PasswordResetToken toDomainEntity(PasswordResetTokenEntity jpaEntity) {
        return new PasswordResetToken(
                jpaEntity.getToken(),
                PhysiotherapistJpaMapper.toModelEntity(jpaEntity.getPhysiotherapist()),
                jpaEntity.getExpiresAt()
        );
    }
}
