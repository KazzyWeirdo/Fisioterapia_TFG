package com.tfg.adapter.out.persistence.indiba;

import com.tfg.indiba.IndibaSession;
import com.tfg.model.indiba.IndibaSessionFactory;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import com.tfg.patient.Patient;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.IndibaSummaryElement;
import com.tfg.pojos.query.PatientSummaryElement;
import com.tfg.port.out.persistence.IndibaSessionRepository;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.PhysiotherapistRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractIndibaRepositoryTest {

    private Patient testPatient;
    private Physiotherapist testPhysiotherapist;
    private IndibaSession testIndibaSession;
    private IndibaSession testIndibaSession2;

    @Autowired
    public IndibaSessionRepository indibaSessionRepository;

    @Autowired
    public PatientRepository patientRepository;

    @Autowired
    public PhysiotherapistRepository physiotherapistRepository;

    @BeforeEach
    void setUp() {
        testPatient = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");

        testPhysiotherapist = PhysiotherapistFactory.createTestPsychiatrist("hola@gmail.com", "ValidPassword123!");

        patientRepository.save(testPatient);

        physiotherapistRepository.save(testPhysiotherapist);

        testIndibaSession = new IndibaSessionFactory().createTestIndibaSession(
                testPatient,
                testPhysiotherapist,
                new Date(2023, 11, 30),
                new Date(2023, 12, 15)
        );

        testIndibaSession2 = new IndibaSessionFactory().createTestIndibaSession(
                testPatient,
                testPhysiotherapist,
                new Date(2023, 10, 30),
                new Date(2023, 11, 15)
        );
    }

    @AfterEach
    void tearDown() {
        indibaSessionRepository.deleteAll();
    }

    @Test
    public void givenExistingPatient_whenFindByPatient_returnIndibaSessions(){
        indibaSessionRepository.save(testIndibaSession);
        indibaSessionRepository.save(testIndibaSession2);

        PageQuery query = new PageQuery(0, 10);

        PagedResponse<IndibaSummaryElement> response = indibaSessionRepository.findAllByPatientId(query, testPatient.getId());

        List<IndibaSummaryElement> indibaSessions = response.content();

        assertThat(response.totalElements()).isEqualTo(2);
        assertThat(response.totalPages()).isEqualTo(1);
        assertThat(response.pageNumber()).isEqualTo(0);
        assertThat(response.isLast()).isTrue();

        assertThat(indibaSessions).hasSize(2);
        assertThat(indibaSessions.get(0).id()).isEqualTo(testIndibaSession.getId().value());
        assertThat(indibaSessions.get(0).beginSession().getTime()).isEqualTo(testIndibaSession.getBeginSession().getTime());
        assertThat(indibaSessions.get(1).id()).isEqualTo(testIndibaSession2.getId().value());
        assertThat(indibaSessions.get(1).beginSession().getTime()).isEqualTo(testIndibaSession2.getBeginSession().getTime());
    }

    @Test
    public void givenNonExistingPatient_whenFindByPatient_returnEmptyList(){
        PageQuery query = new PageQuery(0, 10);

        PagedResponse<IndibaSummaryElement> response = indibaSessionRepository.findAllByPatientId(query, testPatient.getId());

        List<IndibaSummaryElement> indibaSessions = response.content();

        assertThat(indibaSessions).isEmpty();
    }

    @Test
    public void givenExistingId_whenFindById_returnIndibaSession(){
        indibaSessionRepository.save(testIndibaSession);

        var optionalIndibaSession = indibaSessionRepository.findById(testIndibaSession.getId());

        assertThat(optionalIndibaSession).isPresent();
        assertThat(optionalIndibaSession.get().getId()).isEqualTo(testIndibaSession.getId());
    }

    @Test
    public void givenNonExistingId_whenFindById_returnEmptyOptional(){
        var optionalIndibaSession = indibaSessionRepository.findById(testIndibaSession.getId());

        assertThat(optionalIndibaSession).isNotPresent();
    }

    @Test
    public void givenIndibaSession_whenSave_thenCanBeFound(){
        indibaSessionRepository.save(testIndibaSession);

        var optionalIndibaSession = indibaSessionRepository.findById(testIndibaSession.getId());

        assertThat(optionalIndibaSession).isPresent();
        assertThat(optionalIndibaSession.get().getId()).isEqualTo(testIndibaSession.getId());
    }

    @Test
    public void givenMultipleIndibaSessions_whenDeleteAll_thenRepositoryIsEmpty(){
        indibaSessionRepository.save(testIndibaSession);
        indibaSessionRepository.save(testIndibaSession2);

        indibaSessionRepository.deleteAll();

        PageQuery query = new PageQuery(0, 10);

        PagedResponse<IndibaSummaryElement> response = indibaSessionRepository.findAllByPatientId(query, testPatient.getId());

        List<IndibaSummaryElement> indibaSessions = response.content();

        assertThat(indibaSessions).isEmpty();
    }

    @Test
    public void givenExistingIndibaSession_whenFindByYear_returnList() {
        indibaSessionRepository.save(testIndibaSession);
        indibaSessionRepository.save(testIndibaSession2);

        List<Object[]> indibaSessions = indibaSessionRepository.countSessionGroupedByMonth(testPatient.getId(), testIndibaSession.getBeginSession().getYear() + 1900);

        assertThat(indibaSessions).isNotEmpty();
        assertThat(indibaSessions.size()).isEqualTo(2);
    }

    @Test
    public void givenNoIndibaSessions_whenFindByYear_returnEmptyList() {
        List<Object[]> indibaSessions = indibaSessionRepository.countSessionGroupedByMonth(testPatient.getId(), testIndibaSession.getEndSession().getYear() + 1900);

        assertThat(indibaSessions).isEmpty();
    }
}
