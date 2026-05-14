package com.tfg.adapter.out.persistence.passwordresettoken;

import com.tfg.adapter.out.persistence.physiotherapist.PhysiotherapistJpaEntity;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.physiotherapist.PhysiotherapistId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "password_reset_tokens")
public class PasswordResetTokenEntity {
    @Id
    @Column(nullable = false, unique = true)
    private String token;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "physiotherapist_id", nullable = false)
    private PhysiotherapistJpaEntity physiotherapist;
    @Column(nullable = false)
    private LocalDateTime expiresAt;

}
