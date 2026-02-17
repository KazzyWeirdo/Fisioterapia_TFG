package com.tfg.port.out.persistence;

import com.tfg.indiba.IndibaSession;
import com.tfg.indiba.IndibaSessionId;
import com.tfg.patient.PatientId;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IndibaSessionRepository {

    void save (IndibaSession indibaSession);

    void deleteAll();

    Optional<IndibaSession> findById (IndibaSessionId id);

    List<Date> findAllByPatientId (PatientId patientId);
}
