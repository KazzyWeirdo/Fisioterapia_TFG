package com.tfg.port.out.persistence;

import com.tfg.patient.PatientId;
import com.tfg.pni.PniReport;
import com.tfg.pni.PniReportId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PniReportRepository {

    void save (PniReport pniReport);

    void deleteAll();

    Optional<PniReport> findById (PniReportId id);

    List<LocalDate> findAllReportsByPatiendId (PatientId patientId);
}
