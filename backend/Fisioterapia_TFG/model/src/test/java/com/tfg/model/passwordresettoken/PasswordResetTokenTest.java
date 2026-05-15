package com.tfg.model.passwordresettoken;

import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordResetTokenTest {

    @Test
    void givenFutureExpiry_isExpired_returnsFalse() {
        PasswordResetToken token = new PasswordResetToken(
                "some-token",
                PhysiotherapistFactory.createTestPsychiatrist("physio@test.com", "pass"),
                LocalDateTime.now().plusHours(1)
        );

        assertThat(token.isExpired()).isFalse();
    }

    @Test
    void givenPastExpiry_isExpired_returnsTrue() {
        PasswordResetToken token = new PasswordResetToken(
                "some-token",
                PhysiotherapistFactory.createTestPsychiatrist("physio@test.com", "pass"),
                LocalDateTime.now().minusHours(1)
        );

        assertThat(token.isExpired()).isTrue();
    }
}
