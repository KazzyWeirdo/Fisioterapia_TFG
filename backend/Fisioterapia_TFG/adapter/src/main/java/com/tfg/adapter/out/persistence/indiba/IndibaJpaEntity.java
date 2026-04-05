package com.tfg.adapter.out.persistence.indiba;

import com.tfg.adapter.out.persistence.patient.PatientJpaEntity;
import com.tfg.adapter.out.persistence.physiotherapist.PhysiotherapistJpaEntity;
import com.tfg.indiba.IndibaSessionModes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "indiba_sessions")
public class IndibaJpaEntity {
    @Id
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientJpaEntity patient;
    @Column(nullable = false)
    private Date beginSession;
    @Column(nullable = false)
    private Date endSession;
    @Column(nullable = false)
    private String treatedArea;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IndibaSessionModes mode;
    @Column(nullable = false)
    private float intensity;
    private String objective;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "physiotherapist_id", nullable = false)
    private PhysiotherapistJpaEntity physiotherapist;
    private String observations;
}
