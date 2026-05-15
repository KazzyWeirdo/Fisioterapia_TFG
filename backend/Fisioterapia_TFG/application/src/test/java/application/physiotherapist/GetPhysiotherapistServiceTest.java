package application.physiotherapist;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.application.port.out.persistence.PhysiotherapistRepository;
import com.tfg.application.service.physiotherapist.GetPhysiotherapistService;
import com.tfg.model.physiotherapist.Physiotherapist;
import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import com.tfg.model.physiotherapist.PhysiotherapistId;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetPhysiotherapistServiceTest {

    private final PhysiotherapistRepository repository = mock(PhysiotherapistRepository.class);
    private final GetPhysiotherapistService service = new GetPhysiotherapistService(repository);

    private static final Physiotherapist TEST_PHYSIO =
            PhysiotherapistFactory.createTestPsychiatrist("physio@test.com", "password123");

    @Test
    void givenExistingId_getPhysiotherapist_returnsPhysio() {
        PhysiotherapistId id = TEST_PHYSIO.getId();
        when(repository.findById(id)).thenReturn(Optional.of(TEST_PHYSIO));

        Physiotherapist result = service.getPhysiotherapist(id);

        assertThat(result.getName()).isEqualTo(PhysiotherapistFactory.NAME);
        assertThat(result.getSurname()).isEqualTo(PhysiotherapistFactory.SURNAME);
    }

    @Test
    void givenNonExistingId_getPhysiotherapist_throwsInvalidIdException() {
        PhysiotherapistId id = new PhysiotherapistId(999999);
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class, () -> service.getPhysiotherapist(id));
    }
}
