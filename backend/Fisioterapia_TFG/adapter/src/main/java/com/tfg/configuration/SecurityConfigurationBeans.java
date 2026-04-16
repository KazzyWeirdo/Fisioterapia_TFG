package com.tfg.configuration;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.tfg.port.out.persistence.PhysiotherapistRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class SecurityConfigurationBeans {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    PhysiotherapistDetailsService physiotherapistDetailsService(PhysiotherapistRepository physiotherapistRepository) {
        return new PhysiotherapistDetailsService(physiotherapistRepository);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey secretKey = new SecretKeySpec(this.secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusJwtDecoder
                .withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        SecretKey secretKey = new SecretKeySpec(this.secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        JWKSource<SecurityContext> immutableSecret = new ImmutableSecret<>(secretKey);

        return new NimbusJwtEncoder(immutableSecret);
    }
}
