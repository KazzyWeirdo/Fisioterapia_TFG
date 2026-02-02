package com.tfg.model.pni;

import com.tfg.pni.PniReport;

public class PniReportFactory {

    public static Double HOURS_ASLEEP = 7.5;
    public static Double HRV = 65.0;
    public static int STRESS = 3;

    public static PniReport createTestPniReport(com.tfg.patient.Patient patient, java.time.LocalDate reportDate, int NTRS) {
        return new PniReport(
                patient,
                reportDate,
                HOURS_ASLEEP,
                HRV,
                STRESS,
                NTRS
        );
    }
}
