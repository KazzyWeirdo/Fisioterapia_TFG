package com.tfg.adapter.out.persistence.pni;

import com.tfg.adapter.out.persistence.patient.PatientJpaEntity;
import com.tfg.adapter.out.persistence.patient.PatientJpaMapper;

public class PniReportJpaMapper {

    public static PniReportJpaEntity toJpaEntity(com.tfg.pni.PniReport pniReport, PatientJpaEntity patientJpaEntity) {
        PniReportJpaEntity entity = new PniReportJpaEntity();
        entity.setId(pniReport.getId().value());
        entity.setPatient(patientJpaEntity);
        entity.setReportDate(pniReport.getReportDate());
        entity.setHrv(pniReport.getHrv());
        entity.setNtrs(pniReport.getNtrs());
        entity.setHoursAsleep(pniReport.getHours_asleep());
        entity.setStress(pniReport.getStress());
        return entity;
    }

    public static com.tfg.pni.PniReport toModelEntity(PniReportJpaEntity entity) {
        return new com.tfg.pni.PniReport(
                PatientJpaMapper.toModelEntity(entity.getPatient()),
                entity.getHoursAsleep(),
                entity.getHrv(),
                entity.getStress(),
                entity.getNtrs()
        );
    }
}
