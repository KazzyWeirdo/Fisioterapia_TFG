package com.tfg.adapter.in.rest.pni;

import com.tfg.model.pni.PniReport;

import java.time.LocalDate;

public record PniReportWebModel(
        int id,
        int patientId,
        LocalDate reportDate,
        Double hours_asleep,
        Double hrv,
        int stress,
        int ntrs
) {
    static PniReportWebModel fromDomainModel(PniReport pniReport) {
        return new PniReportWebModel(
                pniReport.getId().value(),
                pniReport.getPatient().getId().value(),
                pniReport.getReportDate(),
                pniReport.getHours_asleep(),
                pniReport.getHrv(),
                pniReport.getAns_charge(),
                pniReport.getSleep_score()
        );
    }
}
