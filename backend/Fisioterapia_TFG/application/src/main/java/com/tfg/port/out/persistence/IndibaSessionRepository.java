package com.tfg.port.out.persistence;

import com.tfg.indiba.IndibaSession;
import com.tfg.indiba.IndibaSessionId;
import com.tfg.patient.PatientId;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.IndibaSummaryElement;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IndibaSessionRepository {

    void save (IndibaSession indibaSession);

    void deleteAll();

    Optional<IndibaSession> findById (IndibaSessionId id);

    PagedResponse<IndibaSummaryElement> findAllByPatientId (PageQuery query, PatientId patientId);

    List<Object[]> countSessionGroupedByMonth(PatientId patientId, Integer year);
}
