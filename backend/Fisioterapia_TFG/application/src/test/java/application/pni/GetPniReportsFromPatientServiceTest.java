package application.pni;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.pni.PniReportFactory;
import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import com.tfg.pni.PniReport;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.IndibaSummaryElement;
import com.tfg.pojos.query.PniReportSummaryElement;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.PniReportRepository;
import com.tfg.service.pni.GetPniReportsFromPatientService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class GetPniReportsFromPatientServiceTest {

    private final PniReportRepository pniReportRepository = mock(PniReportRepository.class);
    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final GetPniReportsFromPatientService pniReportService = new GetPniReportsFromPatientService(pniReportRepository, patientRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final PniReportSummaryElement TEST_PNI_REPORT = new PniReportSummaryElement(1, LocalDate.of(2023, 12, 15));
    private static final PniReportSummaryElement TEST_PNI_REPORT_2 = new PniReportSummaryElement(2, LocalDate.of(2023, 12, 16));

    @Test
    public void givenPatientId_whenPniReportsExists_returnPniReports(){
        when(patientRepository.findById(new PatientId(1)))
                .thenReturn(Optional.of(TEST_PATIENT));

        PageQuery query = new PageQuery(0, 2);

        PagedResponse<PniReportSummaryElement> mockedResponse = new PagedResponse<>(
                List.of(TEST_PNI_REPORT, TEST_PNI_REPORT_2), // content
                2L,                                          // totalElements
                1,                                           // totalPages
                0,                                           // pageNumber
                true                                         // isLast
        );

        when(pniReportRepository.findAllReportsByPatiendId(query, new PatientId(1)))
                .thenReturn(mockedResponse);

        PagedResponse<PniReportSummaryElement> result = pniReportService.getPniReportsFromPatient(query, new PatientId(1));

        assertEquals(2, result.totalElements());
        assertEquals(1, result.totalPages());
        assertEquals(2, result.content().size());
        assertEquals(TEST_PNI_REPORT, result.content().get(0));
        assertEquals(TEST_PNI_REPORT_2, result.content().get(1));

        verify(pniReportRepository).findAllReportsByPatiendId(query, new PatientId(1));
    }

    @Test
    public void givenPatientId_whenPniReportsNotExists_returnEmptyList(){
        when(patientRepository.findById(new PatientId(1)))
                .thenReturn(Optional.of(TEST_PATIENT));

        PageQuery query = new PageQuery(0, 2);

        PagedResponse<PniReportSummaryElement> mockedResponse = new PagedResponse<>(
                List.of(), // content empty
                0L,        // totalElements
                0,         // totalPages
                0,         // pageNumber
                true       // isLast
        );

        when(pniReportRepository.findAllReportsByPatiendId(query, new PatientId(1)))
                .thenReturn(mockedResponse);

        PagedResponse<PniReportSummaryElement> result = pniReportService.getPniReportsFromPatient(query, new PatientId(1));

        assertEquals(0, result.totalElements());
        assertEquals(0, result.totalPages());
        assertEquals(0, result.content().size());

        verify(pniReportRepository).findAllReportsByPatiendId(query, new PatientId(1));
    }

    @Test
    public void givenInvalidPatientId_whenGetPniReports_throwException(){
        when(patientRepository.findById(new PatientId(99)))
                .thenReturn(Optional.empty());

        try {
            pniReportService.getPniReportsFromPatient(any(PageQuery.class), new PatientId(99));
        } catch (InvalidIdException e) {
            assert(e.getMessage().equals("The provided ID is invalid."));
        }
    }
}
