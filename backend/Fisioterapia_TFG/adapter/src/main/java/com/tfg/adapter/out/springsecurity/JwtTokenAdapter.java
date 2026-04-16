package com.tfg.adapter.out.springsecurity;

import com.tfg.pojos.springsecurity.AuthenticatedUser;
import com.tfg.port.out.springsecurity.TokenGeneratorPort;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JwtTokenAdapter implements TokenGeneratorPort {

    private final JwtEncoder jwtEncoder;

    public JwtTokenAdapter(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public String generateToken(AuthenticatedUser user) {
        Instant now = Instant.now();

        String scope = String.join(" ", user.roles());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.HOURS))
                .subject(user.subject())
                .claim("scope", scope)
                .build();

        var encoderParameters = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims
        );
        return jwtEncoder.encode(encoderParameters).getTokenValue();
    }
}
