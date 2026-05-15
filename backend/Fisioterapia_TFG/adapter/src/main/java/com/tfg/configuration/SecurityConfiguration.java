package com.tfg.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers.hasScope;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    private final JwtDecoder jwtDecoder;

    @Value("${cors.allowed-origin:}")
    private String corsAllowedOrigin;
    private static final String[] WHITE_LIST_URL = {
            "/physiotherapist/login",
            "/password/forgot",
            "/password/reset",
            "/api-docs",
            "/api-docs/**",
            "/api-docs/swagger-config",
            "/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/api/auth/polar/**",
    };

    public SecurityConfiguration(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/api-docs/**",
                "/api-docs/swagger-config",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/webjars/**"
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .cors(cors -> {
                    if (corsAllowedOrigin == null || corsAllowedOrigin.isBlank()) {
                        cors.disable();
                    } else {
                        cors.configurationSource(request -> {
                            CorsConfiguration config = new CorsConfiguration();
                            config.setAllowedOrigins(List.of(corsAllowedOrigin));
                            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                            config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                            return config;
                        });
                    }
                })
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(WHITE_LIST_URL).permitAll();
                    auth.requestMatchers(HttpMethod.DELETE, "/patients/{id}").access(hasScope("ADMIN"));
                    auth.requestMatchers("/indiba/**").access(hasScope("USER"));
                    auth.requestMatchers("/patients/**").access(hasScope("USER"));
                    auth.requestMatchers("/pni/**").access(hasScope("USER"));
                    auth.requestMatchers("/statistics/**").access(hasScope("USER"));
                    auth.requestMatchers("/training-session/**").access(hasScope("USER"));
                    auth.requestMatchers("/auditlogs/**").access(hasScope("ADMIN"));
                    auth.requestMatchers("/physiotherapist/**").access(hasScope("ADMIN"));
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer((oauth2) -> oauth2.jwt((jwt) -> jwt.decoder(jwtDecoder)))
                .build();
    }
}
