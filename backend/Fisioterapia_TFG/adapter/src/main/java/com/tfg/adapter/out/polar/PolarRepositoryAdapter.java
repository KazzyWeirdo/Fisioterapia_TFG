package com.tfg.adapter.out.polar;

import com.tfg.port.out.polar.PolarRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
        return authUri +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&scope=accesslink.read_daily_activity%20accesslink.read_exercises%20accesslink.read_physical_info" +
                "&state=" + state;
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
}
