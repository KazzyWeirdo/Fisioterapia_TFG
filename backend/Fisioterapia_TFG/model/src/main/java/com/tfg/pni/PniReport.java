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
    private int stress;
    private int sleep_score;
    private List<String> trainingLoads; // TODO: Change to TrainingLoad class when created

    public PniReport(Patient patient, Double hours_asleep, Double hrv, int stress, int sleep_score) {
        checkDomain(sleep_score, hours_asleep, hrv, stress);
        this.id = new PniReportId(ThreadLocalRandom.current().nextInt(1_000_000));
        this.patient = patient;
        this.reportDate = LocalDate.now();
        this.hours_asleep = hours_asleep;
        this.hrv = hrv;
        this.stress = stress;
        this.sleep_score = sleep_score;
        this.trainingLoads = new ArrayList<>();
    }

    private void checkDomain(int sleep_score, Double hours_asleep, Double hrv, int stress) {
        if (sleep_score < 0 || sleep_score > 100) {
            throw new IllegalArgumentException("Sleep score must be between 0 and 100");
        }

        if (hours_asleep < 0) {
            throw new IllegalArgumentException("Hours asleep cannot be negative");
        }

        if (hrv < 0) {
            throw new IllegalArgumentException("HRV cannot be negative");
        }

        if (stress < 0 || stress > 100) {
            throw new IllegalArgumentException("Stress level must be between 0 and 100");
        }
    }
}
