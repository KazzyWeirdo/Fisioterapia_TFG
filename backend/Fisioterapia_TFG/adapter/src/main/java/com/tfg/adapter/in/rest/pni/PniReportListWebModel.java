package com.tfg.adapter.in.rest.pni;

import com.tfg.model.pni.PniReport;

import java.time.LocalDate;

public record PniReportListWebModel(
        int id,
        LocalDate reportDate
) {
    static PniReportListWebModel fromDomainModel(PniReport pniReport) {
        return new PniReportListWebModel(
                pniReport.getId().value(),
                pniReport.getReportDate()
        );
    }
}
