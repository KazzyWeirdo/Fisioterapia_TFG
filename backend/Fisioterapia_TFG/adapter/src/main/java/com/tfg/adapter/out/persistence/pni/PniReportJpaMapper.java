package com.tfg.adapter.out.persistence.pni;

import com.tfg.adapter.out.persistence.patient.PatientJpaEntity;
import com.tfg.adapter.out.persistence.patient.PatientJpaMapper;
import com.tfg.model.pni.PniReport;
import com.tfg.model.pni.PniReportId;

public class PniReportJpaMapper {

    public static PniReportJpaEntity toJpaEntity(PniReport pniReport, PatientJpaEntity patientJpaEntity) {
        PniReportJpaEntity entity = new PniReportJpaEntity();
        entity.setId(pniReport.getId().value());
        entity.setPatient(patientJpaEntity);
        entity.setReportDate(pniReport.getReportDate());
        entity.setHrv(pniReport.getHrv());
        entity.setSleepScore(pniReport.getSleep_score());
        entity.setHoursAsleep(pniReport.getHours_asleep());
        entity.setAnsCharge(pniReport.getAns_charge());
        return entity;
    }

    public static PniReport toModelEntity(PniReportJpaEntity entity) {
        return new PniReport(
                new PniReportId(entity.getId()),
                PatientJpaMapper.toModelEntity(entity.getPatient()),
                entity.getReportDate(),
                entity.getHoursAsleep(),
                entity.getHrv(),
                entity.getAnsCharge(),
                entity.getSleepScore()
        );
    }
}
