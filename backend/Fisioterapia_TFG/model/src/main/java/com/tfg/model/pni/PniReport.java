package com.tfg.model.pni;

import com.tfg.model.patient.Patient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@Getter
@Setter
public class PniReport {
    private final PniReportId id;
    private final Patient patient;
    private LocalDate reportDate;
    private Double hours_asleep;
    private Double avg_hr;
    private int min_hr;
    private int deep_sleep;
    private Double continuity;

    public PniReport(Patient patient, Double hours_asleep, Double avg_hr, int min_hr, int deep_sleep, Double continuity) {
        checkDomain(continuity, hours_asleep, avg_hr, min_hr, deep_sleep);
        this.id = new PniReportId(ThreadLocalRandom.current().nextInt(1_000_000));
        this.patient = patient;
        this.reportDate = LocalDate.now();
        this.hours_asleep = hours_asleep;
        this.avg_hr = avg_hr;
        this.min_hr = min_hr;
        this.deep_sleep = deep_sleep;
        this.continuity = continuity;
    }

    private void checkDomain(Double continuity, Double hours_asleep, Double avg_hr, int min_hr, int deep_sleep) {
        if (continuity < 1.0 || continuity > 5.0) {
            throw new IllegalArgumentException("Continuity must be between 1 and 5");
        }

        if (hours_asleep < 0) {
            throw new IllegalArgumentException("Hours asleep cannot be negative");
        }

        if (avg_hr < 0) {
            throw new IllegalArgumentException("Average HR cannot be negative");
        }

        if (min_hr < 0) {
            throw new IllegalArgumentException("Min HR cannot be negative");
        }

        if (deep_sleep < 0) {
            throw new IllegalArgumentException("Deep sleep cannot be negative");
        }
    }
}
