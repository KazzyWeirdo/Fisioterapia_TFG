package com.tfg.model.auditlog;

import com.tfg.auditlog.AuditLogId;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class AuditLogIdTest {
    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0})
    void givenInvalidValue_newAuditLogId_throwsException(int value) {
        ThrowableAssert.ThrowingCallable invocation = () -> new AuditLogId(value);

        assertThatIllegalArgumentException().isThrownBy(invocation);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 8_765, 2_000_000_000})
    void givenValidValue_newAuditLogId_succeeds(int value) {
        AuditLogId auditLogId = new AuditLogId(value);

        assertThat(auditLogId.value()).isEqualTo(value);
    }
}
