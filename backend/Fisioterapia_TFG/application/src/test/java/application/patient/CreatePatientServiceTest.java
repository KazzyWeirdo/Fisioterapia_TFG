package application.patient;

import com.tfg.patient.*;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.service.patient.CreatePatientService;
import com.tfg.model.patient.PatientFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CreatePatientServiceTest {

    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final CreatePatientService patientService = new CreatePatientService(patientRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");

    @Test
    public void givenNewPatient_whenPatientNotExists_createPatient(){
        when(patientRepository.findByEmail(any(PatientEmail.class)))
                .thenReturn(java.util.Optional.empty());

        when(patientRepository.findByDni(any(PatientDNI.class)))
                .thenReturn(java.util.Optional.empty());

        patientService.createPatient(TEST_PATIENT.getEmail().value(), TEST_PATIENT.getDni().value(), TEST_PATIENT.getGender(), TEST_PATIENT.getName(), TEST_PATIENT.getSurname(), TEST_PATIENT.getSecondSurname(), TEST_PATIENT.getDateOfBirth(), TEST_PATIENT.getPhoneNumber());

        verify(patientRepository).save(argThat(patient ->
                patient.getEmail().value().equals("hola@gmail.com") &&
                        patient.getDni().value().equals("85729487J")
        ));
    }

    @Test
    public void givenNewPatient_withExistingEmail_throwException(){
        when(patientRepository.findByEmail(any(PatientEmail.class)))
                .thenReturn(java.util.Optional.of(TEST_PATIENT));

        assertThrows(IllegalArgumentException.class, () -> {
            patientService.createPatient(TEST_PATIENT.getEmail().value(), "12345678H", TEST_PATIENT.getGender(), TEST_PATIENT.getName(), TEST_PATIENT.getSurname(), TEST_PATIENT.getSecondSurname(), TEST_PATIENT.getDateOfBirth(), TEST_PATIENT.getPhoneNumber());
        });
        verify(patientRepository, never()).save(any());
    }

    @Test
    public void givenNewPatient_withExistingDni_throwException(){
        when(patientRepository.findByEmail(any(PatientEmail.class)))
                .thenReturn(java.util.Optional.empty());

        when(patientRepository.findByDni(any(PatientDNI.class)))
                .thenReturn(java.util.Optional.of(TEST_PATIENT));

        assertThrows(IllegalArgumentException.class, () -> {
            patientService.createPatient("examplemail@gmail.com", TEST_PATIENT.getDni().value(), TEST_PATIENT.getGender(), TEST_PATIENT.getName(), TEST_PATIENT.getSurname(), TEST_PATIENT.getSecondSurname(), TEST_PATIENT.getDateOfBirth(), TEST_PATIENT.getPhoneNumber());
        });
        verify(patientRepository, never()).save(any());
    }
}
