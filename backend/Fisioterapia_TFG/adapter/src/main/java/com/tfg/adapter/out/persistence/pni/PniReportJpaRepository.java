package com.tfg.adapter.out.persistence.pni;

import com.tfg.adapter.out.persistence.indiba.IndibaSummaryJpaProjection;
import com.tfg.adapter.out.persistence.patient.PatientJpaEntity;
import com.tfg.adapter.out.persistence.patient.PatientJpaMapper;
import com.tfg.patient.PatientId;
import com.tfg.pni.PniReport;
import com.tfg.pni.PniReportId;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.IndibaSummaryElement;
import com.tfg.pojos.query.PniReportSummaryElement;
import com.tfg.port.out.persistence.PniReportRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @Transactional
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
    public PagedResponse<PniReportSummaryElement> findAllReportsByPatiendId(PageQuery query, PatientId patientId) {
        Pageable pageable = PageRequest.of(query.page(), query.size());
        Page<PniReportSummaryJpaProjection> page = pniReportJpaDataRepository.findAllByPatientId(patientId.value(), pageable);

        List<PniReportSummaryElement> content = page.getContent().stream()
                .map(proj -> new PniReportSummaryElement(proj.id(), proj.reportDate()))
                .toList();

        return new PagedResponse<>(
                content,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.isLast()
        );
    }

    @Override
    public List<PniReport> findAllForExport() {
        return pniReportJpaDataRepository.findAll().stream()
                .map(PniReportJpaMapper::toModelEntity)
                .toList();
    }
}
