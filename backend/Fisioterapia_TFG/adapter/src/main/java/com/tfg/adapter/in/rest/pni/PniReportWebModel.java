package com.tfg.adapter.in.rest.pni;

import com.tfg.model.pni.PniReport;

import java.time.LocalDate;

public record PniReportWebModel(
        int id,
        int patientId,
        LocalDate reportDate,
        Double hours_asleep,
        Double avg_hr,
        int min_hr,
        int deep_sleep,
        Double continuity
) {
    static PniReportWebModel fromDomainModel(PniReport pniReport) {
        return new PniReportWebModel(
                pniReport.getId().value(),
                pniReport.getPatient().getId().value(),
                pniReport.getReportDate(),
                pniReport.getHours_asleep(),
                pniReport.getAvg_hr(),
                pniReport.getMin_hr(),
                pniReport.getDeep_sleep(),
                pniReport.getContinuity()
        );
    }
}
