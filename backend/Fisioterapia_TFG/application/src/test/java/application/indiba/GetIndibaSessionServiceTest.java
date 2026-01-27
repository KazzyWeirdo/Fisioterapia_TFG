package application.indiba;

import com.tfg.indiba.IndibaSession;
import com.tfg.indiba.IndibaSessionId;
import com.tfg.model.indiba.IndibaSessionFactory;
import com.tfg.port.out.persistence.IndibaSessionRepository;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetIndibaSessionServiceTest {

    private final IndibaSessionRepository indibaSessionRepository = mock(IndibaSessionRepository.class);
    // GetIndibaSessionService should go in here

    private static final IndibaSessionId EXISTING_ID = new IndibaSessionId(1);
    private static final IndibaSessionId NON_EXISTING_ID = new IndibaSessionId(99);
    private static final IndibaSession TEST_INDIBA_SESSION = new IndibaSessionFactory().createTestIndibaSession(EXISTING_ID.value(), 1,new Date(2023, 11, 30), new Date(2023, 12, 15));

    @Test
    public void givenIndibaSessionId_whenIndibaSessionExists_returnIndibaSession(){
        when(indibaSessionRepository.findById(EXISTING_ID))
                .thenReturn(Optional.of(TEST_INDIBA_SESSION));

        IndibaSession result = null; // Replace with service logic here

        assertNotNull(result);
        assertEquals(result.getId(), result.getId());

        verify(indibaSessionRepository).findById(EXISTING_ID);
    }

    @Test
    public void givenIndibaSessionId_whenIndibaSessionNotExists_throwException(){
        when(indibaSessionRepository.findById(NON_EXISTING_ID))
                .thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            // Replace with service logic here
        });
    }
}
