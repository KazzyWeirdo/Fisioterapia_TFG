package com.tfg.port.out.polar;

import com.tfg.pni.PniReport;

import java.util.Optional;

public interface PolarRepository {
    String generateAuthUrl(String state);

    PolarAuthResult exchangeCode(String code);

    Optional<PniReport> fetchDailyData (String polarAccessToken, Long polarUserId);

    record PolarAuthResult(String accessToken, Long polarUserId) {}
}
