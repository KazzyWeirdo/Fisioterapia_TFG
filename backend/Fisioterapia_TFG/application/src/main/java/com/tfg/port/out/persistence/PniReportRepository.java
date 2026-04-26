package com.tfg.port.out.persistence;

import com.tfg.patient.PatientId;
import com.tfg.pni.PniReport;
import com.tfg.pni.PniReportId;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.PniReportSummaryElement;

import java.util.List;
import java.util.Optional;

public interface PniReportRepository {

    void save (PniReport pniReport);

    void deleteAll();

    Optional<PniReport> findById (PniReportId id);

    PagedResponse<PniReportSummaryElement> findAllReportsByPatiendId (PageQuery query, PatientId patientId);

    List<PniReport> findAllForExport();
}
