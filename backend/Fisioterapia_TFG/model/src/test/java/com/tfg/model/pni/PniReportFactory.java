package com.tfg.model.pni;

import com.tfg.pni.PniReport;

public class PniReportFactory {

    public static Double HOURS_ASLEEP = 7.5;
    public static Double HRV = 65.0;
    public static int ANS_CHARGE = 3;

    public static PniReport createTestPniReport(com.tfg.patient.Patient patient, int sleep_score) {
        return new PniReport(
                patient,
                HOURS_ASLEEP,
                HRV,
                ANS_CHARGE,
                sleep_score
        );
    }
}
