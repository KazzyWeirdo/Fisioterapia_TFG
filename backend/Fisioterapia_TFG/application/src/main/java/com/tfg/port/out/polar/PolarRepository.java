package com.tfg.port.out.polar;

public interface PolarRepository {
    String generateAuthUrl(String state);
    PolarAuthResult exchangeCode(String code);
    record PolarAuthResult(String accessToken, Long polarUserId) {}
}
