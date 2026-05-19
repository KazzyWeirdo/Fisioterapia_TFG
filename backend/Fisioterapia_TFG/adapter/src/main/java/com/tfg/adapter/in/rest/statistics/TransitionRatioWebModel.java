package com.tfg.adapter.in.rest.statistics;

import com.tfg.model.statistics.PatientMonthTransitionRatio;

public record TransitionRatioWebModel(
        Integer month,
        Double transitionRatio,
        Long indibaSessions,
        Long trainingSessions
) {
    static TransitionRatioWebModel fromDomainModel(PatientMonthTransitionRatio transitionRatio) {
        return new TransitionRatioWebModel(
                transitionRatio.month(),
                transitionRatio.getTransitionRatio(),
                transitionRatio.indibaSessions(),
                transitionRatio.trainingSessions()
        );
    }
}
