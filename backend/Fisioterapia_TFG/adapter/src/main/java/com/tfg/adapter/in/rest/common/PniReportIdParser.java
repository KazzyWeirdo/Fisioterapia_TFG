package com.tfg.adapter.in.rest.common;

import com.tfg.pni.PniReportId;

public class PniReportIdParser {
    private PniReportIdParser() {}

    public static PniReportId parsePniReportId(String string) {
        try {
            return new PniReportId(Integer.parseInt(string));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
