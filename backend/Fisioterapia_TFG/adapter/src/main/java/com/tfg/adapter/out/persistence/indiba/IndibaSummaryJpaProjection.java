package com.tfg.adapter.out.persistence.indiba;

import java.util.Date;

public record IndibaSummaryJpaProjection(
        int id,
        Date beginSession
) {
}
