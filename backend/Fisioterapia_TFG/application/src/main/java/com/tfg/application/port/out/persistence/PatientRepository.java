package com.tfg.application.port.out.persistence;

import com.tfg.model.patient.Patient;
import com.tfg.model.patient.PatientDNI;
import com.tfg.model.patient.PatientEmail;
import com.tfg.model.patient.PatientId;
import com.tfg.application.pojos.pagedpojos.PageQuery;
import com.tfg.application.pojos.pagedpojos.PagedResponse;
import com.tfg.application.pojos.query.PatientSummaryElement;

import java.util.List;
import java.util.Optional;

public interface PatientRepository {

    void save(Patient patient);

    void deleteAll();

    void deleteById(PatientId patientId);

    void update(PatientId patientId, Patient patient);

    Optional<Patient> findById(PatientId id);

    Optional<Patient> findByEmail(PatientEmail email);

    Optional<Patient> findByDni(PatientDNI dni);

    PagedResponse<PatientSummaryElement> findAllSummaries(PageQuery query);

    List<Patient> findAllWithPolarToken();

    List<Patient> findAll();

    List<Patient> findAllDischarged();
}
