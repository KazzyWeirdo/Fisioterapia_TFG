package com.tfg.application.port.out.persistence;

import com.tfg.model.indiba.IndibaSession;
import com.tfg.model.indiba.IndibaSessionId;
import com.tfg.model.patient.PatientId;
import com.tfg.application.pojos.pagedpojos.PageQuery;
import com.tfg.application.pojos.pagedpojos.PagedResponse;
import com.tfg.application.pojos.query.IndibaSummaryElement;

import java.util.List;
import java.util.Optional;

public interface IndibaSessionRepository {

    void save (IndibaSession indibaSession);

    void deleteAll();

    Optional<IndibaSession> findById (IndibaSessionId id);

    PagedResponse<IndibaSummaryElement> findAllByPatientId (PageQuery query, PatientId patientId);

    List<Object[]> countSessionGroupedByMonth(PatientId patientId, Integer year);

    List<IndibaSession> findAllForExport();

    List<IndibaSession> findAllByPatientId(PatientId patientId);
}
