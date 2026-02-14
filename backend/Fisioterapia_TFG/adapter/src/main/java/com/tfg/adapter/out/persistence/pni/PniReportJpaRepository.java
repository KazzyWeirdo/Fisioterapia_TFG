package com.tfg.adapter.out.persistence.pni;

import com.tfg.adapter.out.persistence.patient.PatientJpaEntity;
import com.tfg.adapter.out.persistence.patient.PatientJpaMapper;
import com.tfg.patient.PatientId;
import com.tfg.pni.PniReport;
import com.tfg.pni.PniReportId;
import com.tfg.port.out.persistence.PniReportRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class PniReportJpaRepository implements PniReportRepository {

    private final PniReportJpaDataRepository pniReportJpaDataRepository;

    public PniReportJpaRepository(PniReportJpaDataRepository pniReportJpaDataRepository) {
        this.pniReportJpaDataRepository = pniReportJpaDataRepository;
    }


    @Override
    public void save(PniReport pniReport) {
        PatientJpaEntity patientJpaEntity = PatientJpaMapper.toJpaEntity(pniReport.getPatient());
        PniReportJpaEntity pniReportJpaEntity = PniReportJpaMapper.toJpaEntity(pniReport, patientJpaEntity);
        pniReportJpaDataRepository.save(pniReportJpaEntity);
    }

    @Override
    public void deleteAll() {
        pniReportJpaDataRepository.deleteAll();
    }

    @Override
    public Optional<PniReport> findById(PniReportId id) {
        Optional<PniReport> pniReport = pniReportJpaDataRepository.findById(id.value())
                .map(PniReportJpaMapper::toModelEntity);
        return pniReport;
    }

    @Override
    public List<LocalDate> findAllReportsByPatiendId(PatientId patientId) {
        List<LocalDate> pniReportsName = pniReportJpaDataRepository.findAllReportDatesByPatientId(patientId.value());
        return pniReportsName;
    }
}
