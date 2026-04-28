package com.tfg.adapter.out.persistence.patient;

import com.tfg.patient.Patient;
import com.tfg.model.patient.PatientFactory;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.PatientSummaryElement;
import com.tfg.port.out.persistence.PatientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import com.tfg.adapter.out.persistence.BaseRepositoryIT;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractPatientRepositoryTest extends BaseRepositoryIT {

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final Patient TEST_PATIENT2 = PatientFactory.createTestPatient("hola2@gmail.com", "85728487G");

    @Autowired
    public PatientRepository patientRepository;

    @AfterEach
    void tearDown() {
        patientRepository.deleteAll();
    }

    @Test
    public void givenAnExistingId_whenFindByiD_returnPatient(){
        patientRepository.save(TEST_PATIENT);
        Optional<Patient> optionalPatient = patientRepository.findById(TEST_PATIENT.getId());

        assertThat(optionalPatient).isPresent();
        assertThat(optionalPatient.get().getId()).isEqualTo(TEST_PATIENT.getId());
        assertThat(optionalPatient.get().getEmail()).isEqualTo(TEST_PATIENT.getEmail());
        assertThat(optionalPatient.get().getDni()).isEqualTo(TEST_PATIENT.getDni());
    }

    @Test
    public void givenAnUnexistingId_whenFindByiD_returnEmptyOptional(){
        Optional<Patient> optionalPatient = patientRepository.findById(TEST_PATIENT.getId());

        assertThat(optionalPatient).isNotPresent();
    }

    @Test
    public void givenAnExistingEmail_whenFindByEmail_returnPatient() {
        patientRepository.save(TEST_PATIENT);
        Optional<Patient> optionalPatient = patientRepository.findByEmail(TEST_PATIENT.getEmail());

        assertThat(optionalPatient).isPresent();
        assertThat(optionalPatient.get().getId()).isEqualTo(TEST_PATIENT.getId());
        assertThat(optionalPatient.get().getEmail()).isEqualTo(TEST_PATIENT.getEmail());
        assertThat(optionalPatient.get().getDni()).isEqualTo(TEST_PATIENT.getDni());
    }

    @Test
    public void givenAnUnexistingEmail_whenFindByEmail_returnEmptyOptional(){
        Optional<Patient> optionalPatient = patientRepository.findByEmail(TEST_PATIENT.getEmail());

        assertThat(optionalPatient).isNotPresent();
    }

    @Test
    public void givenAnExistingDni_whenFindByDni_returnPatient() {
        patientRepository.save(TEST_PATIENT);
        Optional<Patient> optionalPatient = patientRepository.findByDni(TEST_PATIENT.getDni());

        assertThat(optionalPatient).isPresent();
        assertThat(optionalPatient.get().getId()).isEqualTo(TEST_PATIENT.getId());
        assertThat(optionalPatient.get().getEmail()).isEqualTo(TEST_PATIENT.getEmail());
        assertThat(optionalPatient.get().getDni()).isEqualTo(TEST_PATIENT.getDni());
    }

    @Test
    public void givenAnUnexistingDni_whenFindByDni_returnEmptyOptional(){
        Optional<Patient> optionalPatient = patientRepository.findByDni(TEST_PATIENT.getDni());

        assertThat(optionalPatient).isNotPresent();
    }

    @Test
    public void whenDeleteAll_thenNoPatientIsFound(){
        patientRepository.save(TEST_PATIENT);
        patientRepository.save(TEST_PATIENT2);

        patientRepository.deleteAll();

        Optional<Patient> optionalPatient1 = patientRepository.findById(TEST_PATIENT.getId());
        Optional<Patient> optionalPatient2 = patientRepository.findById(TEST_PATIENT2.getId());

        assertThat(optionalPatient1).isNotPresent();
        assertThat(optionalPatient2).isNotPresent();
    }

    @Test
    public void givenUniquePatient_whenCreatingPatient_thenPatientIsSavedWithUniqueId(){
        patientRepository.save(TEST_PATIENT);

        Optional<Patient> optionalPatient = patientRepository.findById(TEST_PATIENT.getId());

        assertThat(optionalPatient).isPresent();
        assertThat(optionalPatient.get().getId()).isEqualTo(TEST_PATIENT.getId());
        assertThat(optionalPatient.get().getEmail()).isEqualTo(TEST_PATIENT.getEmail());
        assertThat(optionalPatient.get().getDni()).isEqualTo(TEST_PATIENT.getDni());
        assertThat(optionalPatient.get().getLegalName()).isEqualTo(TEST_PATIENT.getLegalName());
        assertThat(optionalPatient.get().getNameToUse()).isEqualTo(TEST_PATIENT.getNameToUse());
        assertThat(optionalPatient.get().getSurname()).isEqualTo(TEST_PATIENT.getSurname());
        assertThat(optionalPatient.get().getSecondSurname()).isEqualTo(TEST_PATIENT.getSecondSurname());
        assertThat(optionalPatient.get().getPhoneNumber()).isEqualTo(TEST_PATIENT.getPhoneNumber());
        assertThat(optionalPatient.get().getGenderIdentity()).isEqualTo(TEST_PATIENT.getGenderIdentity());
        assertThat(optionalPatient.get().getAdministrativeSex()).isEqualTo(TEST_PATIENT.getAdministrativeSex());
        assertThat(optionalPatient.get().getPronouns()).isEqualTo(TEST_PATIENT.getPronouns());
        assertThat(optionalPatient.get().getDateOfBirth()).isEqualTo(TEST_PATIENT.getDateOfBirth());

    }
    @Test
    public void givenExistingPatient_whenUpdate_thenPatientIsUpdated() {
        patientRepository.save(TEST_PATIENT);

        Patient updatedPatient = PatientFactory.createTestPatient("updated@gmail.com", "85729487J");
        updatedPatient.setLegalName("UpdatedName");
        updatedPatient.setNameToUse("UpdatedNameToUse");
        updatedPatient.setSurname("UpdatedSurname");
        updatedPatient.setSecondSurname("UpdatedSecondSurname");
        updatedPatient.setPhoneNumber(123456789);
        updatedPatient.setGenderIdentity(TEST_PATIENT.getGenderIdentity());
        updatedPatient.setAdministrativeSex(TEST_PATIENT.getAdministrativeSex());
        updatedPatient.setPronouns(TEST_PATIENT.getPronouns());
        updatedPatient.setDateOfBirth(TEST_PATIENT.getDateOfBirth());

        patientRepository.update(TEST_PATIENT.getId(), updatedPatient);

        Optional<Patient> optionalPatient = patientRepository.findById(TEST_PATIENT.getId());

        assertThat(optionalPatient).isPresent();
        assertThat(optionalPatient.get().getEmail()).isEqualTo(updatedPatient.getEmail());
        assertThat(optionalPatient.get().getLegalName()).isEqualTo(updatedPatient.getLegalName());
        assertThat(optionalPatient.get().getSurname()).isEqualTo(updatedPatient.getSurname());
        assertThat(optionalPatient.get().getSecondSurname()).isEqualTo(updatedPatient.getSecondSurname());
        assertThat(optionalPatient.get().getPhoneNumber()).isEqualTo(updatedPatient.getPhoneNumber());
    }

    @Test
    public void whenSearchingForPatientsWithTokens_thenGiveListOfPatients() {
        TEST_PATIENT.setPolarUserId(1L);
        TEST_PATIENT.setPolarAccessToken("token1");
        TEST_PATIENT2.setPolarAccessToken("token2");
        TEST_PATIENT2.setPolarUserId(2L);
        patientRepository.save(TEST_PATIENT);
        patientRepository.save(TEST_PATIENT2);

        List<Patient> patients = patientRepository.findAllWithPolarToken();

        assertThat(patients).hasSize(2);
        assertThat(patients.getFirst().getId().value()).isEqualTo(TEST_PATIENT.getId().value());
        assertThat(patients.get(0).getPolarAccessToken()).isEqualTo(TEST_PATIENT.getPolarAccessToken());
        assertThat(patients.get(0).getPolarUserId()).isEqualTo(TEST_PATIENT.getPolarUserId());
        assertThat(patients.get(1).getId().value()).isEqualTo(TEST_PATIENT2.getId().value());
        assertThat(patients.get(1).getPolarAccessToken()).isEqualTo(TEST_PATIENT2.getPolarAccessToken());
        assertThat(patients.get(1).getPolarUserId()).isEqualTo(TEST_PATIENT2.getPolarUserId());
    }

    @Test
    public void givenExistingPatients_whenFindAll_returnAll() {
        patientRepository.save(TEST_PATIENT);
        patientRepository.save(TEST_PATIENT2);

        List<Patient> result = patientRepository.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(p -> p.getId().value())
                .containsExactlyInAnyOrder(TEST_PATIENT.getId().value(), TEST_PATIENT2.getId().value());
    }

    @Test
    public void givenNoPatients_whenFindAll_returnEmptyList() {
        List<Patient> result = patientRepository.findAll();
        assertThat(result).isEmpty();
    }

    @Test
    public void givenPageQuery_whenFindAll_thenReturnAllPatients() {
        patientRepository.save(TEST_PATIENT);
        patientRepository.save(TEST_PATIENT2);

        PageQuery query = new PageQuery(0, 10);

        PagedResponse<PatientSummaryElement> response = patientRepository.findAllSummaries(query);

        List<PatientSummaryElement> patients = response.content();

        assertThat(response.totalElements()).isEqualTo(2);
        assertThat(response.totalPages()).isEqualTo(1);
        assertThat(response.pageNumber()).isEqualTo(0);
        assertThat(response.isLast()).isTrue();

        assertThat(patients).hasSize(2);
        assertThat(patients.get(0).id()).isEqualTo(TEST_PATIENT.getId().value());
        assertThat(patients.get(0).name()).isEqualTo(TEST_PATIENT.getNameToUse());
        assertThat(patients.get(0).surname()).isEqualTo(TEST_PATIENT.getSurname());
        assertThat(patients.get(1).id()).isEqualTo(TEST_PATIENT2.getId().value());
        assertThat(patients.get(1).name()).isEqualTo(TEST_PATIENT2.getNameToUse());
        assertThat(patients.get(1).surname()).isEqualTo(TEST_PATIENT2.getSurname());
    }
}
