package com.tfg.port.in.indiba;

public interface CreateIndibaSessionUseCase {
    void createIndibaSession(String patientDni,
                             String physiotherapistDni,
                             String sessionDate,
                             String sessionTime,
                             String sessionNotes
    ); // TODO: Consider using a DTO for better parameter management
}
