package com.tfg.model.pni;

import com.tfg.model.patient.Patient;

public class PniReportFactory {

    public static Double HOURS_ASLEEP = 7.5;
    public static Double AVG_HR = 58.0;
    public static int MIN_HR = 48;
    public static int DEEP_SLEEP = 90;

    public static PniReport createTestPniReport(Patient patient, double continuity) {
        return new PniReport(
                patient,
                HOURS_ASLEEP,
                AVG_HR,
                MIN_HR,
                DEEP_SLEEP,
                continuity
        );
    }
}
