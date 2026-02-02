package com.tfg.pni;

public record PniReportId(int value) {
    public PniReportId {
        if (value < 1) {
            throw new IllegalArgumentException("'id' must be a positive integer");
        }
    }
}
