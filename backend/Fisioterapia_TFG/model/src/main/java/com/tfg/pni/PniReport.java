package com.tfg.pni;

import com.tfg.patient.Patient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@Getter
@Setter
public class PniReport {
    private final PniReportId id;
    private final Patient patient;
    private LocalDate reportDate;
    private Double hours_asleep;
    private Double hrv;
    private int ans_charge;
    private int sleep_score;

    public PniReport(Patient patient, Double hours_asleep, Double hrv, int ans_charge, int sleep_score) {
        checkDomain(sleep_score, hours_asleep, hrv, ans_charge);
        this.id = new PniReportId(ThreadLocalRandom.current().nextInt(1_000_000));
        this.patient = patient;
        this.reportDate = LocalDate.now();
        this.hours_asleep = hours_asleep;
        this.hrv = hrv;
        this.ans_charge = ans_charge;
        this.sleep_score = sleep_score;
    }

    private void checkDomain(int sleep_score, Double hours_asleep, Double hrv, int ans_charge) {
        if (sleep_score < 0 || sleep_score > 100) {
            throw new IllegalArgumentException("Sleep score must be between 0 and 100");
        }

        if (hours_asleep < 0) {
            throw new IllegalArgumentException("Hours asleep cannot be negative");
        }

        if (hrv < 0) {
            throw new IllegalArgumentException("HRV cannot be negative");
        }

        if (ans_charge < 0 || ans_charge > 100) {
            throw new IllegalArgumentException("Ans Charge must be between 0 and 100");
        }
    }
}
