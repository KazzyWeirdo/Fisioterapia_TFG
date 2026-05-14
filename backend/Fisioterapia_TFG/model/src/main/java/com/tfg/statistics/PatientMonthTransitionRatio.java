package com.tfg.statistics;

public record PatientMonthTransitionRatio(
        int month,
        int year,
        long indibaSessions,
        long trainingSessions
) {
    public double getTransitionRatio() {
        long total = indibaSessions + trainingSessions;
        return total == 0 ? 0.0 : (double) trainingSessions / total;
    }
}
