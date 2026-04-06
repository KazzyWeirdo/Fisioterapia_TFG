package com.tfg.adapter.in.rest.pni;

import java.time.LocalDate;

public record PniReportListWebModel(
        int id,
        LocalDate reportDate
) {
    static PniReportListWebModel fromDomainModel(com.tfg.pni.PniReport pniReport) {
        return new PniReportListWebModel(
                pniReport.getId().value(),
                pniReport.getReportDate()
        );
    }
}
