package com.tfg.adapter.in.rest.statistics;

public record TransitionRatioWebModel(
        Integer month,
        Double transitionRatio
) {
    static TransitionRatioWebModel fromDomainModel(com.tfg.statistics.PatientMonthTransitionRatio transitionRatio) {
        return new TransitionRatioWebModel(
                transitionRatio.month(),
                transitionRatio.getTransitionRatio()
        );
    }
}
