package com.tfg.adapter.out.persistence.pni;

import java.time.LocalDate;

public record PniReportSummaryJpaProjection(
        int id,
        LocalDate reportDate
) {
}
