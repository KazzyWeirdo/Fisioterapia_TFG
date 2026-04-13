package com.tfg.adapter.out.persistence.passwordresettoken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenJpaDataRepository extends JpaRepository<PasswordResetTokenEntity, String> {

}
