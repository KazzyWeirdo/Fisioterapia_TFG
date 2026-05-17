package com.tfg.application.port.out.persistence;

import com.tfg.model.patient.PatientId;
import com.tfg.model.pni.PniReport;
import com.tfg.model.pni.PniReportId;
import com.tfg.application.pojos.pagedpojos.PageQuery;
import com.tfg.application.pojos.pagedpojos.PagedResponse;
import com.tfg.application.pojos.query.PniReportSummaryElement;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PniReportRepository {

    void save (PniReport pniReport);

    void deleteAll();

    void deleteAllByPatientId(PatientId patientId);

    Optional<PniReport> findById (PniReportId id);

    PagedResponse<PniReportSummaryElement> findAllReportsByPatiendId (PageQuery query, PatientId patientId);

    List<PniReport> findAllForExport();

    Optional<PniReport> findByPatientIdAndDate(PatientId patientId, LocalDate date);
}
