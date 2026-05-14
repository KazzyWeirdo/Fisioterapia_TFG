package com.tfg.model.pni;

import com.tfg.indiba.IndibaSessionId;
import com.tfg.pni.PniReportId;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class PniReportIdTest {
    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0})
    void givenInvalidValue_newPniReportId_throwsException(int value) {
        ThrowableAssert.ThrowingCallable invocation = () -> new PniReportId(value);

        assertThatIllegalArgumentException().isThrownBy(invocation);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 8_765, 2_000_000_000})
    void givenValidValue_newPniReportId_succeeds(int value) {
        PniReportId pniReportId = new PniReportId(value);

        assertThat(pniReportId.value()).isEqualTo(value);
    }
}
