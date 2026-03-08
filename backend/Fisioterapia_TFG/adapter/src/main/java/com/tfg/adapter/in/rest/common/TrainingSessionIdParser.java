package com.tfg.adapter.in.rest.common;

import com.tfg.trainingsession.TrainingSessionId;

public class TrainingSessionIdParser {
    private TrainingSessionIdParser() {}

    public static TrainingSessionId parseTrainingSessionId(String string) {
        try {
            return new TrainingSessionId(Integer.parseInt(string));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
