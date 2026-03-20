package com.tfg.adapter.in.rest.pni;

import java.time.LocalDate;

public record PniReportListWebModel(
        String patientId,
        LocalDate reportDate
) {
    static PniReportListWebModel fromDomainModel(com.tfg.pni.PniReport pniReport) {
        return new PniReportListWebModel(
                String.valueOf(pniReport.getPatient().getId().value()),
                pniReport.getReportDate()
        );
    }
}
