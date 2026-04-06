package com.tfg.adapter.out.persistence.pni;

import com.tfg.model.patient.PatientFactory;
import com.tfg.model.pni.PniReportFactory;
import com.tfg.patient.Patient;
import com.tfg.pni.PniReport;
import com.tfg.pni.PniReportId;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.IndibaSummaryElement;
import com.tfg.pojos.query.PniReportSummaryElement;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.PniReportRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractPniReportRepositoryTest {

    private Patient testPatient;
    private PniReport testPniReport1;
    private PniReport testPniReport2;

    @Autowired
    public PniReportRepository pniReportRepository;

    @Autowired
    public PatientRepository patientRepository;

    @BeforeEach
    void setUp() {
        testPatient = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");

        patientRepository.save(testPatient);

        testPniReport1 = PniReportFactory.createTestPniReport(testPatient, 6);
        testPniReport2 = PniReportFactory.createTestPniReport(testPatient, 6);
    }

    @AfterEach
    void tearDown() {
        pniReportRepository.deleteAll();
    }

    @Test
    public void givenExistingPatient_whenFindByPatient_returnPniReports(){
        pniReportRepository.save(testPniReport1);
        pniReportRepository.save(testPniReport2);

        PageQuery query = new PageQuery(0, 10);

        PagedResponse<PniReportSummaryElement> response = pniReportRepository.findAllReportsByPatiendId(query, testPatient.getId());

        List<PniReportSummaryElement> pniReports = response.content();

        assertThat(response.totalElements()).isEqualTo(2);
        assertThat(response.totalPages()).isEqualTo(1);
        assertThat(response.pageNumber()).isEqualTo(0);
        assertThat(response.isLast()).isTrue();

        assertThat(pniReports).hasSize(2);
        assertThat(pniReports.get(0).id()).isEqualTo(testPniReport1.getId().value());
        assertThat(pniReports.get(0).date()).isEqualTo(testPniReport1.getReportDate());
        assertThat(pniReports.get(1).id()).isEqualTo(testPniReport2.getId().value());
        assertThat(pniReports.get(1).date()).isEqualTo(testPniReport2.getReportDate());
    }

    @Test
    public void givenNoExistingPatient_whenFindByPatient_returnEmptyList(){
        PageQuery query = new PageQuery(0, 10);

        PagedResponse<PniReportSummaryElement> response = pniReportRepository.findAllReportsByPatiendId(query, testPatient.getId());

        List<PniReportSummaryElement> pniReports = response.content();

        assertThat(pniReports).isEmpty();
    }

    @Test
    public void givenExistingPniReport_whenFindById_returnPniReport(){
        pniReportRepository.save(testPniReport1);

        Optional<PniReport> retrievedPniReport = pniReportRepository.findById(testPniReport1.getId());

        assertThat(retrievedPniReport).isPresent();
    }

    @Test
    public void givenNonExistingPniReport_whenFindById_returnOptionalEmpty(){
        Optional<PniReport> retrievedPniReport = pniReportRepository.findById(new PniReportId(9999));

        assertThat(retrievedPniReport).isNotPresent();
    }

    @Test
    public void givenExistingPniReport_whenDeleteAll_thenRepositoryIsEmpty(){
        pniReportRepository.save(testPniReport1);
        pniReportRepository.save(testPniReport2);

        pniReportRepository.deleteAll();

        PageQuery query = new PageQuery(0, 10);

        PagedResponse<PniReportSummaryElement> response = pniReportRepository.findAllReportsByPatiendId(query, testPatient.getId());

        List<PniReportSummaryElement> pniReports = response.content();

        assertThat(pniReports).isEmpty();
    }

    @Test
    public void givenExistingPniReport_whenSave_thenPniReportIsSaved(){
       pniReportRepository.save(testPniReport1);

       Optional<PniReport> retrievedPniReport = pniReportRepository.findById(testPniReport1.getId());

        assertThat(retrievedPniReport).isPresent();
    }
}
