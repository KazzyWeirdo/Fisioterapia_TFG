package com.tfg.model.indiba;

public record IndibaSessionId(int value) {
    public IndibaSessionId {
        if (value < 1) {
            throw new IllegalArgumentException("'value' must be a positive integer");
        }
    }
}
