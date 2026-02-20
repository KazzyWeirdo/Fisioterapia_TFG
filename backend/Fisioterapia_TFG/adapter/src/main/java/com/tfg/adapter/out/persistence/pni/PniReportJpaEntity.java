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
    private Double hrv;
    private int stress;
    @Column(nullable = false)
    private int sleepScore;
    //@OneToMany(mappedBy = "pni_reports")
    //private List<String> trainingLoads; TODO: Change to TrainingLoad class when created
}
