package application.patient;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.service.patient.UpdatePatientService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

public class UpdatePatientServiceTest {
    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final UpdatePatientService updatePatientService = new UpdatePatientService(patientRepository);

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final Patient TEST_PATIENT_SIMILAR_EMAIL = PatientFactory.createTestPatient("jola@gmail.com", "85729487J");
    private static final Patient TEST_PATIENT_SIMILAR_DNI = PatientFactory.createTestPatient("hola@gmail.com", "85729487G");
    private static final PatientId TEST_PATIENT_ID = TEST_PATIENT.getId();

    @Test
    public void givenExistingPatientId_whenUpdatePatient_updatesSuccessfully() {
        when(patientRepository.findById(TEST_PATIENT_ID))
                .thenReturn(java.util.Optional.of(TEST_PATIENT));

        when(patientRepository.findByEmail(TEST_PATIENT.getEmail()))
                .thenReturn(java.util.Optional.empty());

        when(patientRepository.findByDni(TEST_PATIENT.getDni()))
                .thenReturn(java.util.Optional.empty());

        updatePatientService.updatePatient(TEST_PATIENT_ID, TEST_PATIENT);

        verify(patientRepository).update(eq(TEST_PATIENT_ID), argThat(patient ->
                patient.getEmail().value().equals("hola@gmail.com") &&
                        patient.getDni().value().equals("85729487J")
        ));
    }

    @Test
    public void givenNonExistingPatientId_whenUpdatePatient_throwsException() {
        when(patientRepository.findById(TEST_PATIENT_ID))
                .thenReturn(java.util.Optional.empty());

        assertThrows(InvalidIdException.class, () -> {
            updatePatientService.updatePatient(TEST_PATIENT_ID, TEST_PATIENT);
        });

        verify(patientRepository, never()).update(any(), any());
    }

    @Test
    public void givenExistingEmail_whenUpdatePatient_throwsException() {
        when(patientRepository.findById(TEST_PATIENT_ID))
                .thenReturn(java.util.Optional.of(TEST_PATIENT));

        when(patientRepository.findByEmail(TEST_PATIENT_SIMILAR_EMAIL.getEmail()))
                .thenReturn(java.util.Optional.of(TEST_PATIENT_SIMILAR_EMAIL));

        assertThrows(IllegalArgumentException.class, () -> {
            updatePatientService.updatePatient(TEST_PATIENT_ID, TEST_PATIENT_SIMILAR_EMAIL);
        });

        verify(patientRepository, never()).update(any(), any());
    }

    @Test
    public void givenExistingDni_whenUpdatePatient_throwsException() {
        when(patientRepository.findById(TEST_PATIENT_ID))
                .thenReturn(java.util.Optional.of(TEST_PATIENT));

        when(patientRepository.findByEmail(TEST_PATIENT.getEmail()))
                .thenReturn(java.util.Optional.empty());

        when(patientRepository.findByDni(TEST_PATIENT_SIMILAR_DNI.getDni()))
                .thenReturn(java.util.Optional.of(TEST_PATIENT_SIMILAR_DNI));

        assertThrows(IllegalArgumentException.class, () -> {
            updatePatientService.updatePatient(TEST_PATIENT_ID, TEST_PATIENT_SIMILAR_DNI);
        });

        verify(patientRepository, never()).update(any(), any());
    }
}
