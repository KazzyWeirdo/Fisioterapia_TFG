package com.tfg.adapter.out.polar;

import com.tfg.pni.PniReport;
import com.tfg.port.out.polar.PolarRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.function.Consumer;

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
    public Optional<PniReport> fetchDailyData(String polarAccessToken, Long polarUserId) {
        boolean dataFound = false;
        PniReport result = new PniReport(null, 1.0, 1.0, 1, 1);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(polarAccessToken);
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Obtain Sleep
        try {
            String sleepTxUrl = "https://www.polaraccesslink.com/v3/users/" + polarUserId + "/sleep";
            processTransactionRaw(sleepTxUrl, entity, (jsonRoot) -> {
                String startStr = jsonRoot.get("sleep_start_time").asText();
                String endStr = jsonRoot.get("sleep_end_time").asText();
                ZonedDateTime startTime = ZonedDateTime.parse(startStr);
                ZonedDateTime endTime = ZonedDateTime.parse(endStr);

                Duration duration = Duration.between(startTime, endTime);

                double hours = duration.toMinutes() / 60.0;
                result.setHours_asleep(hours);
            });

            if (result.getHours_asleep() != null) dataFound = true;

            String rechargeTxUrl = "https://www.polaraccesslink.com/v3/users/" + polarUserId + "/nightly-recharge";

            processTransactionRaw(rechargeTxUrl, entity, (jsonRoot) -> {
                JsonNode ansStatus = jsonRoot.path("ans_charge_status");

                if (!ansStatus.isMissingNode()) {
                    if (ansStatus.has("heart_rate_variability_rmssd")) {
                        result.setHrv(ansStatus.get("heart_rate_variability_rmssd").asDouble());
                    }
                    if (ansStatus.has("ans_charge")) {
                        result.setStress(ansStatus.get("ans_charge").asInt());
                    }
                }
            });

            if (result.getHrv() != null) dataFound = true;

        } catch (Exception e) {
            // Ignore exceptions, just means no sleep data for that day
        }
        return dataFound ? Optional.of(result) : Optional.empty();
    }

    protected void processTransactionRaw(String txUrl, HttpEntity<?> entity, Consumer<JsonNode> dataExtractor) {
        try {
            ResponseEntity<JsonNode> txResponse = restTemplate.postForEntity(txUrl, entity, JsonNode.class);

            if (txResponse.getStatusCode() == HttpStatus.NO_CONTENT || txResponse.getBody() == null) {
                return;
            }

            JsonNode txJson = txResponse.getBody();
            String transactionId = txJson.get("transaction-id").asText();
            String resourceUrl = txJson.get("resource-uri").asText();

            ResponseEntity<JsonNode> listResponse = restTemplate.exchange(
                    resourceUrl, HttpMethod.GET, entity, JsonNode.class);

            JsonNode listJson = listResponse.getBody();

            listJson.fields().forEachRemaining(entry -> {
                JsonNode urlArray = entry.getValue();
                if (urlArray.isArray()) {
                    for (JsonNode urlNode : urlArray) {
                        String dataUrl = urlNode.asText();

                        ResponseEntity<JsonNode> dataResponse = restTemplate.exchange(
                                dataUrl, HttpMethod.GET, entity, JsonNode.class);

                        if (dataResponse.getBody() != null) {
                            dataExtractor.accept(dataResponse.getBody());
                        }
                    }
                }
            });

            String commitUrl = txUrl + "/" + transactionId;
            restTemplate.exchange(commitUrl, HttpMethod.PUT, entity, Void.class);

        } catch (Exception e) {
            // Ignore no content responses, just means no data for that transaction
        }
        }
}