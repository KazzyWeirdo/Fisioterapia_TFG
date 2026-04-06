package application.indiba;

import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.IndibaSummaryElement;
import com.tfg.port.out.persistence.IndibaSessionRepository;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.service.indiba.GetIndibaSessionFromPatientService;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class GetIndibaSessionFromPatientServiceTest {

    private final IndibaSessionRepository indibaSessionRepository = mock(IndibaSessionRepository.class);
    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final GetIndibaSessionFromPatientService indibaSessionService = new GetIndibaSessionFromPatientService(indibaSessionRepository, patientRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final IndibaSummaryElement TEST_INDIBA_SESSION_1 = new IndibaSummaryElement(1, new Date(2023, Calendar.DECEMBER, 15));
    private static final IndibaSummaryElement TEST_INDIBA_SESSION_2 = new IndibaSummaryElement(2, new Date(2023, Calendar.DECEMBER, 16));

    @Test
    public void givenPatientId_whenIndibaSessionsExists_returnIndibaSessions(){
        when(patientRepository.findById(new PatientId(1)))
                .thenReturn(Optional.of(TEST_PATIENT));

        PageQuery query = new PageQuery(0, 2);

        PagedResponse<IndibaSummaryElement> mockedResponse = new PagedResponse<>(
                List.of(TEST_INDIBA_SESSION_1, TEST_INDIBA_SESSION_2), // content
                2L,                                          // totalElements
                1,                                           // totalPages
                0,                                           // pageNumber
                true                                         // isLast
        );

        when(indibaSessionRepository.findAllByPatientId(query, new PatientId(1)))
                .thenReturn(mockedResponse);

        PagedResponse<IndibaSummaryElement> result = indibaSessionService.getIndibaSessionsFromPatient(query, new PatientId(1));

        assertEquals(2, result.totalElements());
        assertEquals(1, result.totalPages());
        assertEquals(2, result.content().size());
        assertEquals(TEST_INDIBA_SESSION_1, result.content().get(0));
        assertEquals(TEST_INDIBA_SESSION_2, result.content().get(1));

        verify(indibaSessionRepository).findAllByPatientId(query, new PatientId(1));
    }

    @Test
    public void givenPatientId_whenIndibaSessionsNotExists_returnEmptyList(){
        when(patientRepository.findById(new PatientId(1)))
                .thenReturn(Optional.of(TEST_PATIENT));

        PageQuery query = new PageQuery(0, 2);

        PagedResponse<IndibaSummaryElement> mockedResponse = new PagedResponse<>(
                List.of(), // content empty
                0L,        // totalElements
                0,         // totalPages
                0,         // pageNumber
                true       // isLast
        );

        when(indibaSessionRepository.findAllByPatientId(query, new PatientId(1))).thenReturn(mockedResponse);

        PagedResponse<IndibaSummaryElement> result = indibaSessionService.getIndibaSessionsFromPatient(query, new PatientId(1));

        assertEquals(0, result.totalElements());
        assertEquals(0, result.totalPages());
        assertEquals(0, result.content().size());

        verify(indibaSessionRepository).findAllByPatientId(query, new PatientId(1));
    }

    @Test
    public void givenUnexistingPatientId_whenGetIndibaSessions_throwException(){
        when(patientRepository.findById(new PatientId(99)))
                .thenReturn(Optional.empty());

        PageQuery query = new PageQuery(0, 2);

        try {
            indibaSessionService.getIndibaSessionsFromPatient(query, new PatientId(99));
        } catch (Exception e) {
            assertEquals("The provided ID is invalid.", e.getMessage());
        }
    }
}
