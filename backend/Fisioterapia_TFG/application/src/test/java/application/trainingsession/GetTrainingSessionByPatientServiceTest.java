package application.trainingsession;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.TrainingSessionSummaryElement;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.TrainingSessionRepository;
import com.tfg.service.trainingsession.GetTrainingSessionByPatientService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GetTrainingSessionByPatientServiceTest {

    private final TrainingSessionRepository trainingSessionRepository = mock(TrainingSessionRepository.class);
    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final GetTrainingSessionByPatientService getTrainingSessionByPatientService = new GetTrainingSessionByPatientService(trainingSessionRepository, patientRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "12345678D");
    private static final TrainingSessionSummaryElement TEST_TRAINING_SESSION = new TrainingSessionSummaryElement(1, LocalDate.of(2024, 6, 1));
    private static final TrainingSessionSummaryElement TEST_TRAINING_SESSION_2 = new TrainingSessionSummaryElement(2, LocalDate.of(2024, 6, 2));

    @Test
    public void givenPatiendId_whenTrainingSessionsExists_giveDates() {
        when(patientRepository.findById(new PatientId(1)))
                .thenReturn(Optional.of(TEST_PATIENT));

        PageQuery query = new PageQuery(0, 2);

        PagedResponse<TrainingSessionSummaryElement> mockedResponse = new PagedResponse<>(
                List.of(TEST_TRAINING_SESSION, TEST_TRAINING_SESSION_2), // content
                2L,                                          // totalElements
                1,                                           // totalPages
                0,                                           // pageNumber
                true                                         // isLast
        );

        when(trainingSessionRepository.findAllByPatientId(query, new PatientId(1)))
                .thenReturn(mockedResponse);

        PagedResponse<TrainingSessionSummaryElement> result = trainingSessionRepository.findAllByPatientId(query, new PatientId(1));

        assertEquals(2, result.totalElements());
        assertEquals(1, result.totalPages());
        assertEquals(2, result.content().size());
        assertEquals(TEST_TRAINING_SESSION, result.content().get(0));
        assertEquals(TEST_TRAINING_SESSION_2, result.content().get(1));

        verify(trainingSessionRepository).findAllByPatientId(query, new PatientId(1));
    }

    @Test
    public void givenPatientId_whenNoTrainingSessionsExists_giveEmtpyList() {
        when(patientRepository.findById(new PatientId(1)))
                .thenReturn(Optional.of(TEST_PATIENT));

        PageQuery query = new PageQuery(0, 2);

        PagedResponse<TrainingSessionSummaryElement> mockedResponse = new PagedResponse<>(
                List.of(), // content
                0,                                          // totalElements
                0,                                           // totalPages
                0,                                           // pageNumber
                true                                         // isLast
        );

        when(trainingSessionRepository.findAllByPatientId(query, new PatientId(1)))
                .thenReturn(mockedResponse);

        PagedResponse<TrainingSessionSummaryElement> result = trainingSessionRepository.findAllByPatientId(query, new PatientId(1));

        assertEquals(0, result.totalElements());
        assertEquals(0, result.totalPages());
        assertEquals(0, result.content().size());

        verify(trainingSessionRepository).findAllByPatientId(query, new PatientId(1));
    }

    @Test
    public void givenUnexistingPatientId_throwException() {
        when(patientRepository.findById(new PatientId(1)))
                .thenReturn(Optional.empty());

        try {
            getTrainingSessionByPatientService.getTrainingSessionFromPatient(any(PageQuery.class), new PatientId(1));
        } catch (InvalidIdException e) {
            assertEquals("The provided ID is invalid.", e.getMessage());
        }
    }
}
