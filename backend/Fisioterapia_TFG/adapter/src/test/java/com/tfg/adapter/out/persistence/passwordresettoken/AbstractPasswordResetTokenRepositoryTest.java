package com.tfg.adapter.out.persistence.passwordresettoken;

import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import com.tfg.passwordresettoken.PasswordResetToken;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.port.out.persistence.PasswordResetTokenRepository;
import com.tfg.port.out.persistence.PhysiotherapistRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.tfg.adapter.out.persistence.BaseRepositoryIT;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractPasswordResetTokenRepositoryTest extends BaseRepositoryIT {

    private static final Physiotherapist TEST_PHYSIO = PhysiotherapistFactory.createTestPsychiatrist("hola@gmail.com", "ValidPassword123!");
    private static final String RAW_TOKEN = UUID.randomUUID().toString();
    private static final PasswordResetToken TEST_TOKEN = new PasswordResetToken(
            RAW_TOKEN,
            TEST_PHYSIO,
            LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.MILLIS)
    );

    @Autowired
    public PasswordResetTokenRepository tokenRepository;

    @Autowired
    public PhysiotherapistRepository physiotherapistRepository;

    @AfterEach
    void tearDown() {
        tokenRepository.delete(TEST_TOKEN.token());
        physiotherapistRepository.deleteAll();
    }

    @Test
    public void givenAnExistingToken_whenFindByToken_returnToken() {
        physiotherapistRepository.save(TEST_PHYSIO);
        tokenRepository.save(TEST_TOKEN);

        Optional<PasswordResetToken> optionalToken = tokenRepository.findByToken(TEST_TOKEN.token());

        assertThat(optionalToken).isPresent();
        assertThat(optionalToken.get().token()).isEqualTo(TEST_TOKEN.token());
        assertThat(optionalToken.get().expiresAt()).isEqualTo(TEST_TOKEN.expiresAt());
        assertThat(optionalToken.get().physio().getEmail()).isEqualTo(TEST_PHYSIO.getEmail());
    }

    @Test
    public void givenAnUnexistingToken_whenFindByToken_returnEmptyOptional() {
        String fakeToken = UUID.randomUUID().toString();

        Optional<PasswordResetToken> optionalToken = tokenRepository.findByToken(fakeToken);

        assertThat(optionalToken).isNotPresent();
    }

    @Test
    public void givenUniqueToken_whenCreatingToken_thenTokenIsSaved() {
        physiotherapistRepository.save(TEST_PHYSIO);
        tokenRepository.save(TEST_TOKEN);

        Optional<PasswordResetToken> optionalToken = tokenRepository.findByToken(TEST_TOKEN.token());

        assertThat(optionalToken).isPresent();
        assertThat(optionalToken.get().token()).isEqualTo(TEST_TOKEN.token());
        assertThat(optionalToken.get().physio().getEmail()).isEqualTo(TEST_PHYSIO.getEmail());
    }

    @Test
    public void givenAnExistingToken_whenDelete_thenTokenIsRemoved() {
        physiotherapistRepository.save(TEST_PHYSIO);
        tokenRepository.save(TEST_TOKEN);

        assertThat(tokenRepository.findByToken(TEST_TOKEN.token())).isPresent();

        tokenRepository.delete(TEST_TOKEN.token());

        Optional<PasswordResetToken> optionalToken = tokenRepository.findByToken(TEST_TOKEN.token());
        assertThat(optionalToken).isNotPresent();
    }
}
