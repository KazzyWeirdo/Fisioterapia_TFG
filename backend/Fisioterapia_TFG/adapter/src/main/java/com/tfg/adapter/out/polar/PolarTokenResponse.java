package com.tfg.adapter.out.polar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PolarTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("x_user_id")
    private Long polarUserId;
}