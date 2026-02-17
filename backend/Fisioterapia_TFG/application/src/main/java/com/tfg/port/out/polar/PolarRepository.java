package com.tfg.port.out.polar;

import com.tfg.patient.Patient;
import com.tfg.pni.PniReport;

import java.util.Optional;

public interface PolarRepository {
    String generateAuthUrl(String state);

    PolarAuthResult exchangeCode(String code);

    void registerUserInPolar(String polarAccessToken, Long polarUserId);

    Optional<PniReport> fetchDailyData (Patient patient);

    record PolarAuthResult(String accessToken, Long polarUserId) {}
}
