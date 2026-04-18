package application.patient;

import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.PatientSummaryElement;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.service.patient.GetAllPatientsService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GetAllPatientsServiceTest {
    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final GetAllPatientsService getAllPatientsService = new GetAllPatientsService(patientRepository);

    private static final PatientSummaryElement TEST_PATIENT_1 = new PatientSummaryElement(
            1,
            "John",
            "Doe"
    );

    private static final PatientSummaryElement TEST_PATIENT_2 = new PatientSummaryElement(
            2,
            "Jane",
            "Doe"
    );

    @Test
    public void givenValidPageQuery_whenGetAllAuditLogs_thenReturnPagedResponse() {
        PageQuery query = new PageQuery(0, 2);

        PagedResponse<PatientSummaryElement> mockedResponse = new PagedResponse<>(
                List.of(TEST_PATIENT_1, TEST_PATIENT_2), // content
                2L,                                          // totalElements
                1,                                           // totalPages
                0,                                           // pageNumber
                true                                         // isLast
        );

        when(patientRepository.findAllSummaries(query)).thenReturn(mockedResponse);

        PagedResponse<PatientSummaryElement> result = getAllPatientsService.getAllPatients(query);

        assertEquals(2, result.totalElements());
        assertEquals(1, result.totalPages());
        assertEquals(2, result.content().size());
        assertEquals(TEST_PATIENT_1, result.content().get(0));
        assertEquals(TEST_PATIENT_2, result.content().get(1));

        verify(patientRepository).findAllSummaries(query);
    }

    @Test
    public void givenEmptyPageQuery_whenGetAllAuditLogs_thenReturnEmptyPagedResponse() {
        PageQuery query = new PageQuery(0, 2);

        PagedResponse<PatientSummaryElement> mockedResponse = new PagedResponse<>(
                List.of(), // content empty
                0L,        // totalElements
                0,         // totalPages
                0,         // pageNumber
                true       // isLast
        );

        when(patientRepository.findAllSummaries(query)).thenReturn(mockedResponse);

        PagedResponse<PatientSummaryElement> result = getAllPatientsService.getAllPatients(query);

        assertEquals(0, result.totalElements());
        assertEquals(0, result.totalPages());
        assertEquals(0, result.content().size());

        verify(patientRepository).findAllSummaries(query);
    }
}
