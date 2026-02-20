package com.tfg.adapter.out.polar;

import com.tfg.patient.Patient;
import com.tfg.pni.PniReport;
import com.tfg.port.out.polar.PolarRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
public class PolarRepositoryAdapter implements PolarRepository {

    @Value("${polar.client-id}")
    private String clientId;

    @Value("${polar.client-secret}")
    private String clientSecret;

    @Value("${polar.auth-uri}")
    private String authUri;

    @Value("${polar.token-uri}")
    private String tokenUri;

    @Value("${polar.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String generateAuthUrl(String state) {
        return UriComponentsBuilder.fromHttpUrl(authUri)
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", "accesslink.read_all")
                .queryParam("state", state)
                .build()
                .toUriString();
    }

    @Override
    public PolarAuthResult exchangeCode(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("code", code);
        map.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<PolarTokenResponse> response = restTemplate.postForEntity(
                tokenUri, request, PolarTokenResponse.class);

        PolarTokenResponse body = response.getBody();

        return new PolarAuthResult(body.getAccessToken(), body.getPolarUserId());
    }

    @Override
    public void registerUserInPolar(String accessToken, Long polarMemberId) {
        String url = "https://www.polaraccesslink.com/v3/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/json");
        headers.setBearerAuth(accessToken);

        Map<String, String> body = Collections.singletonMap("member-id", String.valueOf(polarMemberId));

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(url, request, String.class);

            System.out.println("ÉXITO: Usuario Polar " + polarMemberId + " registrado correctamente.");

        } catch (HttpClientErrorException.Conflict e) {
            System.out.println("INFO: El usuario " + polarMemberId + " ya estaba registrado. Continuamos.");

        } catch (HttpClientErrorException e) {
            System.err.println("ERROR: Fallo al registrar usuario en Polar: " + e.getResponseBodyAsString());
            throw e;
        }
    }

    @Override
    public Optional<PniReport> fetchDailyData(Patient patient) {
        LocalDate targetDate = LocalDate.now().minusDays(1);

        String dateStr = targetDate.toString();

        PniReport result = new PniReport(patient, 0.0, 0.0, 0, 0);
        boolean hasSleep = false;
        boolean hasRecharge = false;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(patient.getPolarAccessToken());
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            String sleepUrl = "https://www.polaraccesslink.com/v3/users/sleep/" + dateStr;
            ResponseEntity<JsonNode> response = restTemplate.exchange(sleepUrl, HttpMethod.GET, entity, JsonNode.class);

            if (response.getBody() != null) {
                JsonNode sleepJson = response.getBody();

                String startStr = sleepJson.get("sleep_start_time").asText();
                String endStr = sleepJson.get("sleep_end_time").asText();

                ZonedDateTime startTime = ZonedDateTime.parse(startStr);
                ZonedDateTime endTime = ZonedDateTime.parse(endStr);
                Duration duration = Duration.between(startTime, endTime);

                result.setHours_asleep(duration.toMinutes() / 60.0);
                result.setSleep_score(sleepJson.get("sleep_score").asInt());
                hasSleep = true;
            }
        } catch (HttpClientErrorException.NotFound e) {
            // No hay datos de sueño para hoy, seguimos
        } catch (Exception e) {
            System.err.println("Error fetching Sleep: " + e.getMessage());
        }

        try {
            String rechargeUrl = "https://www.polaraccesslink.com/v3/users/nightly-recharge/" + dateStr;
            ResponseEntity<JsonNode> response = restTemplate.exchange(rechargeUrl, HttpMethod.GET, entity, JsonNode.class);

            if (response.getBody() != null) {
                JsonNode rechargeJson = response.getBody();

                JsonNode ansStatus = rechargeJson.path("ans_charge_status");
                if (!ansStatus.isMissingNode()) {
                    if (ansStatus.has("heart_rate_variability_rmssd")) {
                        result.setHrv(ansStatus.get("heart_rate_variability_rmssd").asDouble());
                    }
                    if (ansStatus.has("ans_charge")) {
                        result.setStress(ansStatus.get("ans_charge").asInt());
                    }
                    hasRecharge = true;
                }
            }
        } catch (HttpClientErrorException.NotFound e) {
            // No hay recharge data
        } catch (Exception e) {
            System.err.println("Error fetching Recharge: " + e.getMessage());
        }

        if (hasSleep || hasRecharge) {
            return Optional.of(result);
        } else {
            return Optional.empty();
        }
    }
}