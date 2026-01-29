package com.tfg.adapter.out.persistence.patient;

import com.tfg.patient.Patient;
import com.tfg.model.patient.PatientFactory;
import com.tfg.port.out.persistence.PatientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractPatientRepositoryTest {

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
        assertThat(optionalPatient.get().getName()).isEqualTo(TEST_PATIENT.getName());
        assertThat(optionalPatient.get().getSurname()).isEqualTo(TEST_PATIENT.getSurname());
        assertThat(optionalPatient.get().getSecondSurname()).isEqualTo(TEST_PATIENT.getSecondSurname());
        assertThat(optionalPatient.get().getPhoneNumber()).isEqualTo(TEST_PATIENT.getPhoneNumber());
        assertThat(optionalPatient.get().getGender()).isEqualTo(TEST_PATIENT.getGender());
        assertThat(optionalPatient.get().getDateOfBirth()).isEqualTo(TEST_PATIENT.getDateOfBirth());

    }
    @Test
    public void givenExistingPatient_whenUpdate_thenPatientIsUpdated() {
        patientRepository.save(TEST_PATIENT);

        Patient updatedPatient = PatientFactory.createTestPatient("updated@gmail.com", "85729487J");
        updatedPatient.setName("UpdatedName");
        updatedPatient.setSurname("UpdatedSurname");
        updatedPatient.setSecondSurname("UpdatedSecondSurname");
        updatedPatient.setPhoneNumber(123456789);
        updatedPatient.setGender(TEST_PATIENT.getGender());
        updatedPatient.setDateOfBirth(TEST_PATIENT.getDateOfBirth());

        patientRepository.update(TEST_PATIENT.getId(), updatedPatient);

        Optional<Patient> optionalPatient = patientRepository.findById(TEST_PATIENT.getId());

        assertThat(optionalPatient).isPresent();
        assertThat(optionalPatient.get().getEmail()).isEqualTo(updatedPatient.getEmail());
        assertThat(optionalPatient.get().getName()).isEqualTo(updatedPatient.getName());
        assertThat(optionalPatient.get().getSurname()).isEqualTo(updatedPatient.getSurname());
        assertThat(optionalPatient.get().getSecondSurname()).isEqualTo(updatedPatient.getSecondSurname());
        assertThat(optionalPatient.get().getPhoneNumber()).isEqualTo(updatedPatient.getPhoneNumber());
    }
}
