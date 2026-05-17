package com.tfg.adapter.out.persistence.pni;

import com.tfg.adapter.out.persistence.patient.PatientJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "pni_reports")
public class PniReportJpaEntity {
    @Id
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientJpaEntity patient;
    @Column(nullable = false)
    private java.time.LocalDate reportDate;
    @Column(nullable = false)
    private Double hoursAsleep;
    private Double avgHr;
    private int minHr;
    private int deepSleep;
    @Column(nullable = false)
    private Double continuity;
}
