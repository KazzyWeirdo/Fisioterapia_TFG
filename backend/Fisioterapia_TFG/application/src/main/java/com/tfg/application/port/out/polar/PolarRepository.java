package com.tfg.application.port.out.polar;

import com.tfg.model.patient.Patient;
import com.tfg.model.pni.PniReport;

import java.time.LocalDate;
import java.util.Optional;

public interface PolarRepository {
    String generateAuthUrl(String state);

    PolarAuthResult exchangeCode(String code);

    void registerUserInPolar(String polarAccessToken, Long polarUserId);

    Optional<PniReport> fetchDailyData(Patient patient, LocalDate date);

    record PolarAuthResult(String accessToken, Long polarUserId) {}
}
