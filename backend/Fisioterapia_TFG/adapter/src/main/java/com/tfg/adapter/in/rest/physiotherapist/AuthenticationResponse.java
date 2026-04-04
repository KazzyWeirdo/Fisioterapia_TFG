package com.tfg.adapter.in.rest.physiotherapist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthenticationResponse {
    @JsonProperty("access_token")
    private String accessToken;
}
